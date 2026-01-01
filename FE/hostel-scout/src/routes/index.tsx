import { createBrowserRouter } from 'react-router-dom';

// Layouts
import { 
  RootLayout,
  GuestLayout, 
  AuthLayout, 
  AdminLayout, 
  HostelOwnerLayout, 
  UserLayout 
} from '../components/layouts';

// Auth Pages
import { SignInPage, 
  SignUpPage 
} from '../pages/auth';

// Guest Pages
import { LandingPage, 
  HostelListPage, 
  HostelDetailPage 
} from '../pages/guest';

// Admin Pages
import { 
  AdminDashboardPage, 
  AdminUsersPage, 
  AdminHostelOwnersPage, 
  AdminHostelsPage, 
  AdminSettingsPage 
} from '../pages/admin';

// Hostel Owner Pages
import { 
  OwnerDashboardPage, 
  OwnerHostelsPage, 
  AddHostelPage, 
  HostelManagePage, 
  OwnerBookingsPage, 
  OwnerProfilePage 
} from '../pages/hostelOwner';

// User Pages
import { 
  UserDashboardPage, 
  UserHostelsPage, 
  UserHostelDetailPage, 
  UserBookingsPage, 
  UserProfilePage 
} from '../pages/user';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <RootLayout />,
    children: [
      // Guest routes (public)
      {
        element: <GuestLayout />,
        children: [
          { index: true, element: <LandingPage /> },
          { path: 'hostels', element: <HostelListPage /> },
          { path: 'hostels/:id', element: <HostelDetailPage /> },
        ],
      },
      
      // Auth routes
      {
        path: 'auth',
        element: <AuthLayout />,
        children: [
          { path: 'signin', element: <SignInPage /> },
          { path: 'signup', element: <SignUpPage /> },
        ],
      },
      
      // Admin routes (protected - TODO: add auth guard)
      {
        path: 'admin',
        element: <AdminLayout />,
        children: [
          { index: true, element: <AdminDashboardPage /> },
          { path: 'dashboard', element: <AdminDashboardPage /> },
          { path: 'users', element: <AdminUsersPage /> },
          { path: 'hostel-owners', element: <AdminHostelOwnersPage /> },
          { path: 'hostels', element: <AdminHostelsPage /> },
          { path: 'settings', element: <AdminSettingsPage /> },
        ],
      },
      
      // Hostel Owner routes (protected - TODO: add auth guard)
      {
        path: 'owner',
        element: <HostelOwnerLayout />,
        children: [
          { index: true, element: <OwnerDashboardPage /> },
          { path: 'dashboard', element: <OwnerDashboardPage /> },
          { path: 'hostels', element: <OwnerHostelsPage /> },
          { path: 'hostels/add', element: <AddHostelPage /> },
          { path: 'hostels/:hostelId', element: <HostelManagePage /> },
          { path: 'bookings', element: <OwnerBookingsPage /> },
          { path: 'profile', element: <OwnerProfilePage /> },
        ],
      },
      
      // User routes (protected - TODO: add auth guard)
      {
        path: 'user',
        element: <UserLayout />,
        children: [
          { index: true, element: <UserDashboardPage /> },
          { path: 'dashboard', element: <UserDashboardPage /> },
          { path: 'hostels', element: <UserHostelsPage /> },
          { path: 'hostels/:id', element: <UserHostelDetailPage /> },
          { path: 'bookings', element: <UserBookingsPage /> },
          { path: 'profile', element: <UserProfilePage /> },
        ],
      },
    ],
  },
]);
