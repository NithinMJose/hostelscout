import './AdminHostelsPage.css';

interface AdminHostel {
  id: number;
  name: string;
  owner: string;
  location: string;
  status: string;
}

export const AdminHostelsPage = () => {
  // TODO: Fetch hostels from backend
  const hostels: AdminHostel[] = [
    { id: 1, name: 'Sunrise PG', owner: 'Owner One', location: 'Bangalore', status: 'active' },
    { id: 2, name: 'Green Valley', owner: 'Owner Two', location: 'Chennai', status: 'pending' },
  ];

  return (
    <div className="page hostels-page">
      <h1>Manage Hostels</h1>
      
      <table className="data-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Owner</th>
            <th>Location</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {hostels.map((hostel) => (
            <tr key={hostel.id}>
              <td>{hostel.name}</td>
              <td>{hostel.owner}</td>
              <td>{hostel.location}</td>
              <td>{hostel.status}</td>
              <td>
                <button className="btn ghost">View</button>
                <button className="btn ghost">Approve</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
