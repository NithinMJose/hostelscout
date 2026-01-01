import './UserHostelsPage.css';
import { Link } from 'react-router-dom';

interface Hostel {
  id: number;
  name: string;
  location: string;
  price: number;
}

export const UserHostelsPage = () => {
  // TODO: Fetch hostels from backend
  const hostels: Hostel[] = [
    { id: 1, name: 'Sunrise PG', location: 'Koramangala, Bangalore', price: 8000 },
    { id: 2, name: 'Green Valley Hostel', location: 'HSR Layout, Bangalore', price: 9500 },
    { id: 3, name: 'Student Haven', location: 'Indiranagar, Bangalore', price: 10000 },
  ];

  return (
    <div className="page hostel-list-page">
      <h1>Browse Hostels</h1>
      <p>Find your perfect accommodation</p>
      
      <div className="hostel-grid">
        {hostels.map((hostel) => (
          <div key={hostel.id} className="hostel-card">
            <div className="hostel-image">Image Placeholder</div>
            <div className="hostel-info">
              <h3>{hostel.name}</h3>
              <p>{hostel.location}</p>
              <p className="price">₹{hostel.price}/month</p>
              <Link to={`/user/hostels/${hostel.id}`} className="btn primary">View Details</Link>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
