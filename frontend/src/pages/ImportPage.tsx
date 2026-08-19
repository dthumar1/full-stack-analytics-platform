import React, { useState } from 'react';

export const ImportPage: React.FC = () => {
  const [file, setFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState('');

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      setFile(e.target.files[0]);
    }
  };

  const handleUpload = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!file) return;

    setUploading(true);
    setMessage('');

    try {
      const formData = new FormData();
      formData.append('file', file);
      // TODO: Implement CSV import API call
      setMessage('CSV import feature - API endpoint to be implemented');
    } catch (error) {
      setMessage('Upload failed');
    } finally {
      setUploading(false);
    }
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Import/Export</h1>
      <div className="bg-white p-6 rounded-lg shadow">
        <h2 className="text-xl font-semibold mb-4">Import CSV</h2>
        <form onSubmit={handleUpload} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Select CSV File</label>
            <input
              type="file"
              accept=".csv"
              onChange={handleFileChange}
              className="w-full px-3 py-2 border rounded-md"
            />
          </div>
          <button
            type="submit"
            disabled={!file || uploading}
            className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 disabled:bg-gray-400"
          >
            {uploading ? 'Uploading...' : 'Upload'}
          </button>
        </form>
        {message && <div className="mt-4 text-sm">{message}</div>}
      </div>

      <div className="bg-white p-6 rounded-lg shadow mt-6">
        <h2 className="text-xl font-semibold mb-4">Export Data</h2>
        <p className="text-gray-600 mb-4">Export operational records, employees, or workflows to CSV format.</p>
        <div className="space-x-2">
          <button className="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700">
            Export Records
          </button>
          <button className="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700">
            Export Employees
          </button>
          <button className="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700">
            Export Workflows
          </button>
        </div>
      </div>
    </div>
  );
};
