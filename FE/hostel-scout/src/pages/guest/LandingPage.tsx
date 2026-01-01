import { Link } from 'react-router-dom';
import './LandingPage.css';

export const LandingPage = () => {
  return (
    <>
      <section className="hero">
        <div className="hero-content">
          <h1 className="hero-title">Find and manage PGs with confidence</h1>
          <p className="hero-sub">
            HostelScout connects PG owners and seekers — list rooms, manage bookings, 
            and browse verified hostels nearby.
          </p>

          <div className="hero-ctas">
            <Link to="/auth/signup" className="btn primary">List your PG</Link>
            <Link to="/hostels" className="btn ghost">Browse hostels</Link>
          </div>
        </div>
        <div className="hero-illustration" aria-hidden>
          <div className="phone-mock">HostelScout</div>
        </div>
      </section>

      <section id="features" className="features">
        <h2>Why HostelScout</h2>
        <div className="feature-list">
          <div className="feature">
            <h3>Verified Listings</h3>
            <p>Only trusted PGs with clear photos, amenities, and owner contact details.</p>
          </div>
          <div className="feature">
            <h3>Easy Management</h3>
            <p>Owners can list rooms, approve bookings, and track availability — all in one place.</p>
          </div>
          <div className="feature">
            <h3>Search Nearby</h3>
            <p>Find PGs near colleges, workplaces or transit lines using simple filters.</p>
          </div>
        </div>
      </section>
    </>
  );
};
