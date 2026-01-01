import './AdminUsersPage.css';

interface User {
  id: number;
  name: string;
  email: string;
  role: string;
  status: string;
}

export const AdminUsersPage = () => {
  // TODO: Fetch users from backend
  const users: User[] = [
    { id: 1, name: 'John Doe', email: 'john@example.com', role: 'user', status: 'active' },
    { id: 2, name: 'Jane Smith', email: 'jane@example.com', role: 'user', status: 'active' },
  ];

  return (
    <div className="page users-page">
      <h1>Manage Users</h1>
      
      <table className="data-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.name}</td>
              <td>{user.email}</td>
              <td>{user.role}</td>
              <td>{user.status}</td>
              <td>
                <button className="btn ghost">Edit</button>
                <button className="btn ghost">Disable</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
