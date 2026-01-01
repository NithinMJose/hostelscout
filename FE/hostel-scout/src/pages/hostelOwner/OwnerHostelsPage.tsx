import './OwnerHostelsPage.css';
import { Link } from 'react-router-dom';

interface OwnerHostel {
  id: number;
  name: string;
  location: string;
  rooms: number;
  occupied: number;
  status: 'active' | 'pending';
}

export const OwnerHostelsPage = () => {
  // TODO: Fetch owner's hostels from backend
  const hostels: OwnerHostel[] = [
    { id: 1, name: 'Sunrise PG', location: 'Koramangala', rooms: 15, occupied: 12, status: 'active' },
    { id: 2, name: 'Green Valley', location: 'HSR Layout', rooms: 20, occupied: 18, status: 'active' },
    { id: 3, name: 'Student Haven', location: 'Indiranagar', rooms: 10, occupied: 8, status: 'pending' },
  ];

  return (
    <div className="page hostels-page">
      <div className="page-header">
        <h1>My Hostels</h1>
        <Link to="/owner/hostels/add" className="btn primary">Add New Hostel</Link>
      </div>
      
      <div className="hostel-grid">
        {hostels.map((hostel) => (
          <div key={hostel.id} className="hostel-card">
            <div className="hostel-image">Image Placeholder</div>
            <div className="hostel-info">
              <h3>{hostel.name}</h3>
              <p>{hostel.location}</p>
              <p>{hostel.occupied}/{hostel.rooms} rooms occupied</p>
              <span className={`status ${hostel.status}`}>{hostel.status}</span>
              <div className="card-actions">
                <Link to={`/owner/hostels/${hostel.id}`} className="btn ghost">Manage</Link>
                <Link to={`/owner/hostels/${hostel.id}/rooms`} className="btn ghost">Rooms</Link>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
