import './OwnerDashboardPage.css';

export const OwnerDashboardPage = () => {
  return (
    <div className="page dashboard-page">
      <h1>Owner Dashboard</h1>
      
      <div className="stats-grid">
        <div className="stat-card">
          <h3>My Hostels</h3>
          <p className="stat-value">3</p>
        </div>
        <div className="stat-card">
          <h3>Total Rooms</h3>
          <p className="stat-value">45</p>
        </div>
        <div className="stat-card">
          <h3>Occupied</h3>
          <p className="stat-value">38</p>
        </div>
        <div className="stat-card">
          <h3>Pending Bookings</h3>
          <p className="stat-value">5</p>
        </div>
      </div>
      
      <section className="recent-activity">
        <h2>Recent Bookings</h2>
        <p>No recent bookings to display.</p>
      </section>
    </div>
  );
};
