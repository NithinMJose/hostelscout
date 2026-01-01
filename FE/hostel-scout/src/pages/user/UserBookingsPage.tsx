import './UserBookingsPage.css';

interface UserBooking {
  id: number;
  hostel: string;
  room: string;
  date: string;
  status: 'pending' | 'confirmed' | 'completed' | 'cancelled';
}

export const UserBookingsPage = () => {
  // TODO: Fetch user bookings from backend
  const bookings: UserBooking[] = [
    { id: 1, hostel: 'Sunrise PG', room: '101', date: '2026-01-15', status: 'confirmed' },
    { id: 2, hostel: 'Green Valley', room: '202', date: '2025-12-01', status: 'completed' },
  ];

  return (
    <div className="page bookings-page">
      <h1>My Bookings</h1>
      
      {bookings.length === 0 ? (
        <p>No bookings yet.</p>
      ) : (
        <table className="data-table">
          <thead>
            <tr>
              <th>Hostel</th>
              <th>Room</th>
              <th>Booking Date</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {bookings.map((booking) => (
              <tr key={booking.id}>
                <td>{booking.hostel}</td>
                <td>{booking.room}</td>
                <td>{booking.date}</td>
                <td>{booking.status}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};
