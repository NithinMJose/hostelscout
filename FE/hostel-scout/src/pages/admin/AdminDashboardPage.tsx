import './AdminDashboardPage.css';

export const AdminDashboardPage = () => {
  return (
    <div className="page dashboard-page">
      <h1>Admin Dashboard</h1>
      
      <div className="stats-grid">
        <div className="stat-card">
          <h3>Total Users</h3>
          <p className="stat-value">1,234</p>
        </div>
        <div className="stat-card">
          <h3>Hostel Owners</h3>
          <p className="stat-value">56</p>
        </div>
        <div className="stat-card">
          <h3>Total Hostels</h3>
          <p className="stat-value">89</p>
        </div>
        <div className="stat-card">
          <h3>Pending Approvals</h3>
          <p className="stat-value">12</p>
        </div>
      </div>
      
      <section className="recent-activity">
        <h2>Recent Activity</h2>
        <p>No recent activity to display.</p>
      </section>
    </div>
  );
};
