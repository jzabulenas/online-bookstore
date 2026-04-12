import { useEffect, useState } from "react";

export default function useLocalStorage<T>(key: string): T | null {
  const [storedValue, setStoredValue] = useState<T | null>(() => {
    const value = localStorage.getItem(key);
    return value ? (JSON.parse(value) as T) : null;
  });

  useEffect(() => {
    const handleStorageChange = () => {
      const value = localStorage.getItem(key);
      setStoredValue(value ? (JSON.parse(value) as T) : null);
    };

    window.addEventListener("storage", handleStorageChange);

    return () => {
      window.removeEventListener("storage", handleStorageChange);
    };
  }, [key]);

  return storedValue;
}
