
import { Routes, Route, Navigate } from 'react-router-dom'
import './App.css'
import Layout from './componentes/Layout'
import Dashboard from './pages/Dashboard'
import Reports from './pages/Reports'
import Users from './pages/Users'
import Products from './pages/Products'
import Settings from './pages/Settings'
import SalesReports from './pages/SalesReports'
import UserReports from './pages/UserReports'
import Form from './pages/Form'
import LoginForm from './pages/LoginForm'
import ApiExample from './pages/ApiExample'
import HookExample from './hooks/useApi'
import LoadingStates from './pages/LoadingStates'
import HooksExample from './pages/HooksExample'
import CustomHooksExample from './pages/CustomHooksExample'

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
      <Route index element={<Dashboard />} />
      <Route path="reports" element={<Reports />} />
      <Route path="reports/sales" element={<SalesReports />} />
      <Route path="reports/users" element={<UserReports />} />
      <Route path="users" element={<Users />} />
      <Route path="products" element={<Products />} />
      <Route path="api-example" element={<ApiExample />} />
      <Route path="api-hooks" element={<HookExample />} />
      <Route path="loading-states" element={<LoadingStates />} />
      <Route path="hooks-example" element={<HooksExample />} />
      <Route path="custom-hooks" element={<CustomHooksExample />} />
      <Route path="settings" element={<Settings />} />
      <Route path="form" element={<Form />} />
      <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
      <Route path="register" element={<Form />} />
    </Routes>
  )
}

export default App
