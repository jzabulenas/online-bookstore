import { useEffect, useState } from "react";

export default function useSessionStorage(key) {
  const [storedValue, setStoredValue] = useState(() => {
    const value = sessionStorage.getItem(key);
    return value ? JSON.parse(value) : null;
  });

  useEffect(() => {
    const handleStorageChange = () => {
      const value = sessionStorage.getItem(key);
      setStoredValue(value ? JSON.parse(value) : null);
    };

    window.addEventListener("storage", handleStorageChange);

    return () => {
      window.removeEventListener("storage", handleStorageChange);
    };
  }, [key]);

  return storedValue;
}
