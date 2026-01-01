import { Outlet, Link } from 'react-router-dom';
import './GuestLayout.css';

export const GuestLayout = () => {
  return (
    <div className="content-layout">
      <main className="main-content">
        <Outlet />
      </main>
      
      <footer className="site-footer">
        <div>
          <strong>HostelScout</strong>
          <p>Helping owners and tenants connect since 2025.</p>
        </div>
        <div className="footer-links">
          <Link to="/privacy" className="footer-link">Privacy</Link>
          <Link to="/terms" className="footer-link">Terms</Link>
        </div>
      </footer>
    </div>
  );
};
