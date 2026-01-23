import { useState } from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';
import Topbar from './Topbar';
import LoginForm from '../pages/LoginForm';
import { useLocalStorage } from '../hooks/customHooks';


const Layout = () => {
    const [sidebarOpen, setSidebarOpen] = useState(true); // Inicia ABERTO

    const toggleSidebar = () => {
        console.log('Toggle sidebar clicked! Current state:', sidebarOpen);
        setSidebarOpen(prev => !prev);
    };

    console.log('Layout render - sidebarOpen:', sidebarOpen);

    const [getAuthToken, setAuthToken] = useLocalStorage('authToken', null);

    const logado = !!getAuthToken;

    return (
        <>
        {!logado && (<LoginForm />)}

        {logado && (
            <div className="page">
                {/* Navbar (Topbar) */}
                <Topbar onToggleSidebar={toggleSidebar} />

                {/* Wrapper da página */}
                <div className="page-wrapper">
                    <div className="container-xl">
                        <div className="row g-2 g-md-3">
                            {/* Sidebar - mostra/esconde baseado no estado */}
                            {sidebarOpen && (
                                <div className="col-12 col-md-3 col-lg-2 mt-4 sidebar-container">
                                    <Sidebar
                                        onNavigate={() => {
                                            // Só fecha em mobile (tela pequena)
                                            if (window.innerWidth < 768) {
                                                setSidebarOpen(false);
                                            }
                                        }}
                                    />
                                </div>
                            )}

                            <div className='col-12 mt-4 main-content col-md-9 col-lg-10'>
                                <Outlet />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )}
        </>

    );
}

export default Layout;