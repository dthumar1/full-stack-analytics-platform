# AWS Deployment Guide

This guide covers deploying the Analytics Platform to AWS using various AWS services.

## Prerequisites

- AWS account with appropriate permissions
- AWS CLI installed and configured
- Docker installed locally
- Domain name (optional, for Route 53)

## Deployment Options

### Option 1: AWS ECS (Elastic Container Service)

Recommended for production deployments with auto-scaling.

#### 1. Create ECR Repository

```bash
# Login to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

# Create repositories
aws ecr create-repository --repository-name analytics-backend --region us-east-1
aws ecr create-repository --repository-name analytics-frontend --region us-east-1
```

#### 2. Build and Push Images

```bash
# Backend
docker build -t analytics-backend ./backend
docker tag analytics-backend:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/analytics-backend:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/analytics-backend:latest

# Frontend
docker build -t analytics-frontend ./frontend
docker tag analytics-frontend:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/analytics-frontend:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/analytics-frontend:latest
```

#### 3. Create RDS PostgreSQL Instance

```bash
aws rds create-db-instance \
  --db-instance-identifier analytics-db \
  --db-instance-class db.t3.micro \
  --engine postgres \
  --engine-version 15.4 \
  --master-username analytics \
  --master-user-password <secure-password> \
  --allocated-storage 20 \
  --vpc-security-group-ids <sg-id> \
  --publicly-accessible false
```

#### 4. Create ECS Task Definition

Create `ecs-task-definition.json`:

```json
{
  "family": "analytics-platform",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "512",
  "memory": "1024",
  "containerDefinitions": [
    {
      "name": "backend",
      "image": "<account-id>.dkr.ecr.us-east-1.amazonaws.com/analytics-backend:latest",
      "portMappings": [{"containerPort": 8080}],
      "environment": [
        {
          "name": "SPRING_DATASOURCE_URL",
          "value": "jdbc:postgresql://<rds-endpoint>:5432/analytics_platform"
        },
        {
          "name": "SPRING_DATASOURCE_USERNAME",
          "value": "analytics"
        },
        {
          "name": "SPRING_DATASOURCE_PASSWORD",
          "value": "<rds-password>"
        },
        {
          "name": "JWT_SECRET",
          "value": "<jwt-secret>"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/analytics-platform",
          "awslogs-region": "us-east-1",
          "awslogs-stream-prefix": "backend"
        }
      }
    },
    {
      "name": "frontend",
      "image": "<account-id>.dkr.ecr.us-east-1.amazonaws.com/analytics-frontend:latest",
      "portMappings": [{"containerPort": 80}],
      "dependsOn": [
        {
          "containerName": "backend",
          "condition": "START"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/analytics-platform",
          "awslogs-region": "us-east-1",
          "awslogs-stream-prefix": "frontend"
        }
      }
    }
  ]
}
```

Register the task definition:

```bash
aws ecs register-task-definition --cli-input-json file://ecs-task-definition.json
```

#### 5. Create ECS Cluster

```bash
aws ecs create-cluster --cluster-name analytics-cluster
```

#### 6. Create ECS Service

```bash
aws ecs create-service \
  --cluster analytics-cluster \
  --service-name analytics-service \
  --task-definition analytics-platform \
  --desired-count 1 \
  --launch-type FARGATE \
  --network-configuration "awsvpcConfiguration={subnets=[<subnet-id>],securityGroups=[<sg-id>],assignPublicIp=ENABLED}" \
  --load-balancers "targetGroupArn=<target-group-arn>,containerName=frontend,containerPort=80"
```

#### 7. Configure Application Load Balancer

```bash
# Create ALB
aws elbv2 create-load-balancer \
  --name analytics-alb \
  --subnets <subnet-id-1> <subnet-id-2> \
  --security-groups <sg-id>

# Create target group
aws elbv2 create-target-group \
  --name analytics-tg \
  --port 80 \
  --protocol HTTP \
  --vpc-id <vpc-id> \
  --target-type ip
```

### Option 2: AWS App Runner

Simpler option for quick deployments without managing infrastructure.

#### 1. Push Images to ECR

Follow the same ECR steps as Option 1.

#### 2. Create Backend App Runner Service

```bash
aws apprunner create-service \
  --service-name analytics-backend \
  --source-configuration '{
    "ImageRepository": {
      "ImageIdentifier": "<account-id>.dkr.ecr.us-east-1.amazonaws.com/analytics-backend:latest",
      "ImageRepositoryType": "ECR",
      "ImageConfiguration": {
        "Port": "8080",
        "EnvironmentVariables": [
          {"name": "SPRING_DATASOURCE_URL", "value": "jdbc:postgresql://<rds-endpoint>:5432/analytics_platform"},
          {"name": "SPRING_DATASOURCE_USERNAME", "value": "analytics"},
          {"name": "SPRING_DATASOURCE_PASSWORD", "value": "<rds-password>"},
          {"name": "JWT_SECRET", "value": "<jwt-secret>"}
        ]
      }
    }
  }' \
  --instance-configuration 'Cpu=512,Memory=1024'
```

#### 3. Create Frontend App Runner Service

```bash
aws apprunner create-service \
  --service-name analytics-frontend \
  --source-configuration '{
    "ImageRepository": {
      "ImageIdentifier": "<account-id>.dkr.ecr.us-east-1.amazonaws.com/analytics-frontend:latest",
      "ImageRepositoryType": "ECR",
      "ImageConfiguration": {
        "Port": "80"
      }
    }
  }'
```

### Option 3: AWS Elastic Beanstalk

Traditional deployment option with managed platform.

#### 1. Create Application

```bash
aws elasticbeanstalk create-application --application-name analytics-platform
```

#### 2. Create Environment for Backend

```bash
aws elasticbeanstalk create-environment \
  --application-name analytics-platform \
  --environment-name analytics-backend \
  --solution-stack-name "64bit Amazon Linux 2023 v4.0.0 running Docker" \
  --option-settings Namespace=aws:elasticbeanstalk:container:docker,OptionName=Image,Value=<account-id>.dkr.ecr.us-east-1.amazonaws.com/analytics-backend:latest
```

#### 3. Create Environment for Frontend

```bash
aws elasticbeanstalk create-environment \
  --application-name analytics-platform \
  --environment-name analytics-frontend \
  --solution-stack-name "64bit Amazon Linux 2023 v4.0.0 running Docker" \
  --option-settings Namespace=aws:elasticbeanstalk:container:docker,OptionName=Image,Value=<account-id>.dkr.ecr.us-east-1.amazonaws.com/analytics-frontend:latest
```

## Database Setup

### RDS PostgreSQL

```bash
# Create parameter group
aws rds create-db-parameter-group \
  --db-parameter-group-name analytics-params \
  --db-parameter-group-family postgres15 \
  --description "Analytics platform parameters"

# Modify parameters
aws rds modify-db-parameter-group \
  --db-parameter-group-name analytics-params \
  --parameters "ParameterName=max_connections,ParameterValue=100,ApplyMethod=immediate"

# Create security group
aws ec2 create-security-group \
  --group-name analytics-db-sg \
  --description "Security group for analytics database"

# Allow inbound traffic from ECS
aws ec2 authorize-security-group-ingress \
  --group-id <sg-id> \
  --protocol tcp \
  --port 5432 \
  --source-group <ecs-sg-id>
```

### Initialize Database

Connect to RDS and run the schema and sample data scripts:

```bash
psql -h <rds-endpoint> -U analytics -d analytics_platform -f database/schema.sql
psql -h <rds-endpoint> -U analytics -d analytics_platform -f database/sample-data.sql
```

## Security Configuration

### Secrets Management

Use AWS Secrets Manager for sensitive data:

```bash
# Store database credentials
aws secretsmanager create-secret \
  --name analytics/db-credentials \
  --secret-string '{"username":"analytics","password":"<secure-password>"}'

# Store JWT secret
aws secretsmanager create-secret \
  --name analytics/jwt-secret \
  --secret-string '{"secret":"<jwt-secret>"}'
```

Update application to use secrets:

```bash
aws ecs update-service \
  --cluster analytics-cluster \
  --service analytics-service \
  --task-definition analytics-platform \
  --enable-execute-command
```

### IAM Roles

Create IAM roles for ECS tasks:

```bash
# Create role
aws iam create-role \
  --role-name ecsTaskExecutionRole \
  --assume-role-policy-document file://trust-policy.json

# Attach policies
aws iam attach-role-policy \
  --role-name ecsTaskExecutionRole \
  --policy-arn arn:aws:iam::aws:policy/AmazonECSTaskExecutionRolePolicy

# Add secrets manager access
aws iam put-role-policy \
  --role-name ecsTaskExecutionRole \
  --policy-name SecretsManagerAccess \
  --policy-document file://secrets-policy.json
```

## Monitoring and Logging

### CloudWatch Logs

```bash
# Create log group
aws logs create-log-group --log-group-name /ecs/analytics-platform

# Create log streams
aws logs create-log-stream --log-group-name /ecs/analytics-platform --log-stream-name backend
aws logs create-log-stream --log-group-name /ecs/analytics-platform --log-stream-name frontend
```

### CloudWatch Alarms

```bash
# Create alarm for CPU utilization
aws cloudwatch put-metric-alarm \
  --alarm-name analytics-backend-cpu \
  --alarm-description "Alert on CPU > 80%" \
  --metric-name CPUUtilization \
  --namespace AWS/ECS \
  --statistic Average \
  --period 300 \
  --threshold 80 \
  --comparison-operator GreaterThanThreshold \
  --dimensions Name=ServiceName,Value=analytics-service

# Create alarm for memory utilization
aws cloudwatch put-metric-alarm \
  --alarm-name analytics-backend-memory \
  --alarm-description "Alert on Memory > 80%" \
  --metric-name MemoryUtilization \
  --namespace AWS/ECS \
  --statistic Average \
  --period 300 \
  --threshold 80 \
  --comparison-operator GreaterThanThreshold \
  --dimensions Name=ServiceName,Value=analytics-service
```

## CI/CD Integration

Update `.github/workflows/ci-cd.yml` for AWS deployment:

```yaml
deploy:
  needs: build
  runs-on: ubuntu-latest
  if: github.ref == 'refs/heads/main'
  
  steps:
    - uses: actions/checkout@v3
    
    - name: Configure AWS credentials
      uses: aws-actions/configure-aws-credentials@v2
      with:
        aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
        aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
        aws-region: us-east-1
    
    - name: Login to Amazon ECR
      id: login-ecr
      uses: aws-actions/amazon-ecr-login@v1
    
    - name: Build and push backend
      env:
        ECR_REGISTRY: ${{ steps.login-ecr.outputs.registry }}
        ECR_REPOSITORY: analytics-backend
        IMAGE_TAG: ${{ github.sha }}
      run: |
        docker build -t $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG ./backend
        docker push $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG
    
    - name: Build and push frontend
      env:
        ECR_REGISTRY: ${{ steps.login-ecr.outputs.registry }}
        ECR_REPOSITORY: analytics-frontend
        IMAGE_TAG: ${{ github.sha }}
      run: |
        docker build -t $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG ./frontend
        docker push $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG
    
    - name: Update ECS service
      run: |
        aws ecs update-service --cluster analytics-cluster --service analytics-service --force-new-deployment
```

## Cost Optimization

### RDS

- Use reserved instances for production
- Enable Multi-AZ for high availability
- Use appropriate instance class (db.t3.micro for dev, db.t3.medium for prod)

### ECS

- Use Fargate Spot for non-critical workloads
- Auto-scale based on CPU/memory metrics
- Right-size task definitions

### Storage

- Use EBS gp3 for cost-effective storage
- Enable lifecycle policies for old backups
- Compress log files

## Backup and Disaster Recovery

### RDS Backups

```bash
# Enable automated backups
aws rds modify-db-instance \
  --db-instance-identifier analytics-db \
  --backup-retention-period 7 \
  --apply-immediately

# Create manual snapshot
aws rds create-db-snapshot \
  --db-instance-identifier analytics-db \
  --db-snapshot-identifier analytics-snapshot-$(date +%Y%m%d)
```

### EBS Snapshots

```bash
# Create snapshot
aws ec2 create-snapshot \
  --volume-id <volume-id> \
  --description "Analytics platform backup $(date +%Y%m%d)"
```

## Troubleshooting

### Common Issues

1. **ECS Task Failing to Start**
   - Check CloudWatch logs for error messages
   - Verify environment variables are set correctly
   - Ensure security groups allow necessary traffic

2. **Database Connection Issues**
   - Verify RDS is accessible from ECS
   - Check security group rules
   - Verify credentials in Secrets Manager

3. **High CPU/Memory Usage**
   - Review CloudWatch metrics
   - Consider scaling up task definition
   - Optimize database queries

### Debug Commands

```bash
# View ECS task logs
aws logs tail /ecs/analytics-platform/backend --follow

# Describe ECS service
aws ecs describe-services --cluster analytics-cluster --services analytics-service

# Describe RDS instance
aws rds describe-db-instances --db-instance-identifier analytics-db

# View CloudWatch metrics
aws cloudwatch get-metric-statistics \
  --namespace AWS/ECS \
  --metric-name CPUUtilization \
  --dimensions Name=ServiceName,Value=analytics-service \
  --start-time $(date -u -d '1 hour ago' +%Y-%m-%dT%H:%M:%SZ) \
  --end-time $(date -u +%Y-%m-%dT%H:%M:%SZ) \
  --period 300 \
  --statistics Average
```

## Rollback Procedure

```bash
# Revert to previous task definition
aws ecs update-service \
  --cluster analytics-cluster \
  --service analytics-service \
  --task-definition analytics-platform:<previous-version>

# Restore database from snapshot
aws rds restore-db-instance-from-db-snapshot \
  --db-instance-selector analytics-db \
  --db-snapshot-identifier analytics-snapshot-<date>
```
