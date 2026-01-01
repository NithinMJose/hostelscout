// Type definitions for API responses, models, etc.

export interface User {
  id: string;
  name: string;
  email: string;
  role: 'admin' | 'hostelOwner' | 'user';
  status: 'active' | 'inactive' | 'pending';
  createdAt: string;
}

export interface Hostel {
  id: string;
  name: string;
  location: string;
  address: string;
  description: string;
  pricePerMonth: number;
  totalRooms: number;
  availableRooms: number;
  amenities: string[];
  images: string[];
  ownerId: string;
  status: 'active' | 'pending' | 'inactive';
  createdAt: string;
}

export interface Room {
  id: string;
  hostelId: string;
  roomNumber: string;
  capacity: number;
  occupied: number;
  price: number;
  status: 'available' | 'occupied' | 'maintenance';
}

export interface Booking {
  id: string;
  userId: string;
  hostelId: string;
  roomId: string;
  checkInDate: string;
  checkOutDate?: string;
  status: 'pending' | 'confirmed' | 'rejected' | 'completed' | 'cancelled';
  createdAt: string;
}

export interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
}
