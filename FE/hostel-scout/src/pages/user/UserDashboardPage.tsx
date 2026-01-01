import './UserDashboardPage.css';
import { Link } from 'react-router-dom';

export const UserDashboardPage = () => {
  return (
    <div className="page dashboard-page">
      <h1>Welcome back!</h1>
      
      <div className="quick-actions">
        <Link to="/user/hostels" className="action-card">
          <h3>Browse Hostels</h3>
          <p>Find your perfect accommodation</p>
        </Link>
        <Link to="/user/bookings" className="action-card">
          <h3>My Bookings</h3>
          <p>View your booking history</p>
        </Link>
      </div>
      
      <section className="recent-activity">
        <h2>Recent Activity</h2>
        <p>No recent activity to display.</p>
      </section>
    </div>
  );
};
