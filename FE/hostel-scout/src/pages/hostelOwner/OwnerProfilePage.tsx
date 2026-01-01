import './OwnerProfilePage.css';

interface OwnerProfile {
  name: string;
  email: string;
  phone: string;
  joinedDate: string;
}

export const OwnerProfilePage = () => {
  // TODO: Fetch owner profile from backend
  const profile: OwnerProfile = {
    name: 'Owner Name',
    email: 'owner@example.com',
    phone: '+91 9876543210',
    joinedDate: '2025-06-15',
  };

  return (
    <div className="page profile-page">
      <h1>My Profile</h1>
      
      <div className="profile-card">
        <div className="profile-avatar">
          <div className="avatar-placeholder">ON</div>
        </div>
        
        <div className="profile-info">
          <h2>{profile.name}</h2>
          <p>Email: {profile.email}</p>
          <p>Phone: {profile.phone}</p>
          <p>Member since: {profile.joinedDate}</p>
        </div>
        
        <button className="btn primary">Edit Profile</button>
      </div>
    </div>
  );
};
