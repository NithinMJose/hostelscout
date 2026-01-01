import './UserProfilePage.css';

interface UserProfile {
  name: string;
  email: string;
  phone: string;
  joinedDate: string;
}

export const UserProfilePage = () => {
  // TODO: Fetch user profile from backend
  const profile: UserProfile = {
    name: 'User Name',
    email: 'user@example.com',
    phone: '+91 9876543210',
    joinedDate: '2025-10-01',
  };

  return (
    <div className="page profile-page">
      <h1>My Profile</h1>
      
      <div className="profile-card">
        <div className="profile-avatar">
          <div className="avatar-placeholder">UN</div>
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
