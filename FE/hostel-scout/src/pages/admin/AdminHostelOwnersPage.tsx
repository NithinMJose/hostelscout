import './AdminHostelOwnersPage.css';

interface HostelOwner {
  id: number;
  name: string;
  email: string;
  hostels: number;
  status: string;
}

export const AdminHostelOwnersPage = () => {
  // TODO: Fetch hostel owners from backend
  const owners: HostelOwner[] = [
    { id: 1, name: 'Owner One', email: 'owner1@example.com', hostels: 3, status: 'verified' },
    { id: 2, name: 'Owner Two', email: 'owner2@example.com', hostels: 1, status: 'pending' },
  ];

  return (
    <div className="page owners-page">
      <h1>Manage Hostel Owners</h1>
      
      <table className="data-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Hostels</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {owners.map((owner) => (
            <tr key={owner.id}>
              <td>{owner.name}</td>
              <td>{owner.email}</td>
              <td>{owner.hostels}</td>
              <td>{owner.status}</td>
              <td>
                <button className="btn ghost">View</button>
                <button className="btn ghost">Verify</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
