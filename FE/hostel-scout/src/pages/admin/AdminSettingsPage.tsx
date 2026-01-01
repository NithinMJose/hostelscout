import './AdminSettingsPage.css';

export const AdminSettingsPage = () => {
  return (
    <div className="page settings-page">
      <h1>Admin Settings</h1>
      
      <section className="settings-section">
        <h2>General Settings</h2>
        <p>Configure application-wide settings.</p>
        {/* TODO: Add settings form */}
      </section>
      
      <section className="settings-section">
        <h2>Email Configuration</h2>
        <p>Configure email notifications and templates.</p>
        {/* TODO: Add email settings */}
      </section>
    </div>
  );
};
