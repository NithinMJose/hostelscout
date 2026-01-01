import './OwnerBookingsPage.css';

interface Booking {
  id: number;
  user: string;
  hostel: string;
  room: string;
  date: string;
  status: 'pending' | 'confirmed' | 'cancelled';
}

export const OwnerBookingsPage = () => {
  // TODO: Fetch bookings from backend
  const bookings: Booking[] = [
    { id: 1, user: 'John Doe', hostel: 'Sunrise PG', room: '101', date: '2026-01-15', status: 'pending' },
    { id: 2, user: 'Jane Smith', hostel: 'Green Valley', room: '202', date: '2026-01-10', status: 'confirmed' },
  ];

  return (
    <div className="page bookings-page">
      <h1>Bookings</h1>
      
      <table className="data-table">
        <thead>
          <tr>
            <th>User</th>
            <th>Hostel</th>
            <th>Room</th>
            <th>Date</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {bookings.map((booking) => (
            <tr key={booking.id}>
              <td>{booking.user}</td>
              <td>{booking.hostel}</td>
              <td>{booking.room}</td>
              <td>{booking.date}</td>
              <td>{booking.status}</td>
              <td>
                <button className="btn ghost">Approve</button>
                <button className="btn ghost">Reject</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
