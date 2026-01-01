import './HostelDetailPage.css';
import { useParams, Link } from 'react-router-dom';

interface HostelDetail {
  id: string | undefined;
  name: string;
  location: string;
  price: number;
  description: string;
  amenities: string[];
  rooms: number;
  available: number;
}

export const HostelDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  
  // TODO: Fetch hostel details from backend
  const hostel: HostelDetail = {
    id,
    name: 'Sunrise PG',
    location: 'Koramangala, Bangalore',
    price: 8000,
    description: 'A comfortable PG with all modern amenities.',
    amenities: ['WiFi', 'AC', 'Food', 'Laundry', 'Parking'],
    rooms: 10,
    available: 3,
  };

  return (
    <div className="page hostel-detail-page">
      <Link to="/hostels" className="btn ghost">← Back to Hostels</Link>
      
      <div className="hostel-detail">
        <div className="hostel-images">
          <div className="image-placeholder">Image Gallery Placeholder</div>
        </div>
        
        <div className="hostel-content">
          <h1>{hostel.name}</h1>
          <p className="location">{hostel.location}</p>
          <p className="price">₹{hostel.price}/month</p>
          
          <div className="description">
            <h3>Description</h3>
            <p>{hostel.description}</p>
          </div>
          
          <div className="amenities">
            <h3>Amenities</h3>
            <ul>
              {hostel.amenities.map((amenity) => (
                <li key={amenity}>{amenity}</li>
              ))}
            </ul>
          </div>
          
          <div className="availability">
            <p>{hostel.available} of {hostel.rooms} rooms available</p>
          </div>
          
          <Link to="/auth/signin" className="btn primary">Sign in to Book</Link>
        </div>
      </div>
    </div>
  );
};
