import { useState, type ChangeEvent, type FormEvent } from 'react';
import './AddHostelPage.css';
import { useNavigate } from 'react-router-dom';
import { Input, Button } from '../../components/common';

interface HostelFormData {
  name: string;
  location: string;
  address: string;
  description: string;
  pricePerMonth: string;
  totalRooms: string;
}

export const AddHostelPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<HostelFormData>({
    name: '',
    location: '',
    address: '',
    description: '',
    pricePerMonth: '',
    totalRooms: '',
  });

  const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>): void => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e: FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    // TODO: Submit to backend
    console.log('Add hostel:', formData);
    navigate('/owner/hostels');
  };

  return (
    <div className="page add-hostel-page">
      <h1>Add New Hostel</h1>
      
      <form onSubmit={handleSubmit} className="form-container">
        <Input
          id="name"
          name="name"
          label="Hostel Name"
          placeholder="Enter hostel name"
          value={formData.name}
          onChange={handleChange}
          required
        />
        
        <Input
          id="location"
          name="location"
          label="Location/Area"
          placeholder="e.g., Koramangala, Bangalore"
          value={formData.location}
          onChange={handleChange}
          required
        />
        
        <div className="input-group">
          <label htmlFor="address">Full Address</label>
          <textarea
            id="address"
            name="address"
            placeholder="Enter complete address"
            value={formData.address}
            onChange={handleChange}
            className="input"
            rows={3}
            required
          />
        </div>
        
        <div className="input-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            placeholder="Describe your hostel, amenities, rules, etc."
            value={formData.description}
            onChange={handleChange}
            className="input"
            rows={5}
          />
        </div>
        
        <Input
          id="pricePerMonth"
          name="pricePerMonth"
          type="number"
          label="Price per Month (₹)"
          placeholder="e.g., 8000"
          value={formData.pricePerMonth}
          onChange={handleChange}
          required
        />
        
        <Input
          id="totalRooms"
          name="totalRooms"
          type="number"
          label="Total Rooms"
          placeholder="e.g., 20"
          value={formData.totalRooms}
          onChange={handleChange}
          required
        />
        
        <div className="form-actions">
          <Button type="button" variant="ghost" onClick={() => navigate('/owner/hostels')}>
            Cancel
          </Button>
          <Button type="submit" variant="primary">Add Hostel</Button>
        </div>
      </form>
    </div>
  );
};
