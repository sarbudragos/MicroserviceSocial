import {Navigate, useParams} from 'react-router-dom';
import {useMemo} from "react";

export default function ProtectedRoute({ children, token }) {
    if (!token) {
        return <Navigate to="/login" replace />;
    }

    return children;
}