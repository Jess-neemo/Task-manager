import { useEffect, useState } from "react";
import api from "../api/axios";
import { getRole } from "../utils/jwt";

export default function Dashboard() {

  const [tasks, setTasks] = useState([]);

  const role = getRole();

  const fetchTasks = async () => {

    try {
      const res = await api.get("/tasks/all");
      setTasks(res.data);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  return (
    <div className="p-6">

      <h1 className="text-2xl font-bold mb-2">
        Task Dashboard
      </h1>

      <h2 className="mb-4 text-gray-600">
        Logged in as: {role}
      </h2>

      <div className="grid gap-4">

        {tasks.map(task => (
          <div
            key={task.id}
            className="border p-4 rounded shadow"
          >
            <h2 className="font-bold">{task.title}</h2>
            <p>{task.description}</p>
            <p className="text-sm text-gray-500">
              Status: {task.status}
            </p>
          </div>
        ))}

      </div>

    </div>
  );
}