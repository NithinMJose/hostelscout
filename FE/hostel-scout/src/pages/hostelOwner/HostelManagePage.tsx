import './HostelManagePage.css';
import { useParams, Link } from 'react-router-dom';

interface ManagedHostel {
  id: string | undefined;
  name: string;
  location: string;
  rooms: number;
  occupied: number;
  status: string;
}

export const HostelManagePage = () => {
  const { hostelId } = useParams<{ hostelId: string }>();
  
  // TODO: Fetch hostel details from backend
  const hostel: ManagedHostel = {
    id: hostelId,
    name: 'Sunrise PG',
    location: 'Koramangala, Bangalore',
    rooms: 15,
    occupied: 12,
    status: 'active',
  };

  return (
    <div className="page hostel-manage-page">
      <Link to="/owner/hostels" className="btn ghost">← Back to Hostels</Link>
      
      <h1>Manage: {hostel.name}</h1>
      
      <div className="stats-grid">
        <div className="stat-card">
          <h3>Total Rooms</h3>
          <p className="stat-value">{hostel.rooms}</p>
        </div>
        <div className="stat-card">
          <h3>Occupied</h3>
          <p className="stat-value">{hostel.occupied}</p>
        </div>
        <div className="stat-card">
          <h3>Available</h3>
          <p className="stat-value">{hostel.rooms - hostel.occupied}</p>
        </div>
      </div>
      
      <div className="action-cards">
        <Link to={`/owner/hostels/${hostelId}/rooms`} className="action-card">
          <h3>Manage Rooms</h3>
          <p>Add, edit, or remove rooms</p>
        </Link>
        <Link to={`/owner/hostels/${hostelId}/edit`} className="action-card">
          <h3>Edit Details</h3>
          <p>Update hostel information</p>
        </Link>
        <Link to={`/owner/hostels/${hostelId}/bookings`} className="action-card">
          <h3>Bookings</h3>
          <p>View and manage bookings</p>
        </Link>
      </div>
    </div>
  );
};
