import './App.css'

function App() {
  return (
    <div id="root">
      <header className="nav">
        <div className="brand">HostelScout</div>
        <nav className="nav-actions">
          <a className="btn ghost" href="#features">Features</a>
          <a className="btn" href="#contact">Contact</a>
        </nav>
      </header>

      <main className="hero">
        <div className="hero-content">
          <h1 className="hero-title">Find and manage PGs with confidence</h1>
          <p className="hero-sub">HostelScout connects PG owners and seekers — list rooms, manage bookings, and browse verified hostels nearby.</p>

          <div className="hero-ctas">
            <a className="btn primary" href="#list-your-pg">List your PG</a>
            <a className="btn ghost" href="#browse">Browse hostels</a>
          </div>
        </div>
        <div className="hero-illustration" aria-hidden>
          <div className="phone-mock">HostelScout</div>
        </div>
      </main>

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

      <footer id="contact" className="site-footer">
        <div>
          <strong>HostelScout</strong>
          <p>Helping owners and tenants connect since 2025.</p>
        </div>
        <div className="footer-actions">
          <a href="#" className="link">Privacy</a>
          <a href="#" className="link">Terms</a>
        </div>
      </footer>
    </div>
  )
}

export default App
