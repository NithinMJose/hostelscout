import { Outlet } from 'react-router-dom';
import './UserLayout.css';

export const UserLayout = () => {
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
      </footer>
    </div>
  );
};
