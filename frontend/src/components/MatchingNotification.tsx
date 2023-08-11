import { BellIcon } from '@heroicons/react/24/outline';
import {
  Badge,
  Menu,
  MenuHandler,
  MenuItem,
  MenuList,
} from '@material-tailwind/react';
import { useEffect, useState } from 'react';

// eslint-disable-next-line no-redeclare
type Notification = {
  title: string;
  content: string;
};

const MatchingNotification = () => {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  useEffect(() => {
    const eventSource = new EventSource(
      'https://i9a107.p.ssafy.io/api/notification/subscribe',
    );
    eventSource.onmessage = (e) => {
      const newNotification = JSON.parse(e.data);
      setNotifications((prevNotification) => [
        newNotification,
        ...prevNotification,
      ]);
    };

    eventSource.onerror = (error) => {
      console.error('SSE error: ', error);
    };

    return () => {
      eventSource.close();
    };
  }, [setNotifications]);

  return (
    <Menu>
      <MenuHandler>
        {notifications.length > 0 ? (
          <Badge className="min-w-[5px] min-h-[5px] -translate-x-[40%] translate-y-[10%]">
            <BellIcon className="w-auto cursor-pointer h-7" />
          </Badge>
        ) : (
          <BellIcon className="w-auto cursor-pointer h-7" />
        )}
      </MenuHandler>
      <MenuList>
        {/* TODO: 알림 어떻게 바뀌는지에 따라 모름 */}
        {notifications.length > 0 ? (
          notifications.map((notification, index) => (
            <MenuItem
              key={index}
              className="flex items-center gap-2 border-none bg-none hover:outline-none hover:border-none"
            >
              <div>{notification.title}</div>
              <div>{notification.content}</div>
            </MenuItem>
          ))
        ) : (
          <MenuItem className="flex justify-center">알림이 없습니다</MenuItem>
        )}
      </MenuList>
    </Menu>
  );
};

export default MatchingNotification;
