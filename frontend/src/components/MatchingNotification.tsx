import {
  Menu,
  MenuHandler,
  MenuItem,
  MenuList,
} from '@material-tailwind/react';
import { ReactComponent as Bell } from '../assets/notification.svg';
import { useEffect, useState } from 'react';
import { useRecoilValue } from 'recoil';
import { userState } from '../recoil/user/userState';

// eslint-disable-next-line no-redeclare
type Notification = {
  title: string;
  content: string;
};

const MatchingNotification = () => {
  const user = useRecoilValue(userState);
  const [notifications, setNotifications] = useState<Notification[]>([]);

  const [listening, setListening] = useState<boolean>(false);

  useEffect(() => {
    if (!listening) {
      const eventSource = new EventSource(
        `https://i9a107.p.ssafy.io/api/notification/auth/subscribe/${user.userId}`,
      );

      eventSource.onmessage = async (e) => {
        const data = await e.data;
        const newNotification = JSON.parse(data);
        setNotifications((prevNotification) => [
          newNotification,
          ...prevNotification,
        ]);
      };

      eventSource.onerror = (error) => {
        console.error('SSE error: ', error);
        eventSource.close();
        setListening(false);
      };

      setListening(true);

      return () => {
        eventSource.close();
      };
    }
  }, []);

  return (
    <Menu>
      <MenuHandler>
        {notifications.length > 1 ? (
          <div className="relative w-auto cursor-pointer">
            <Bell className="h-7" />
            <div className="absolute inline-flex items-center justify-center w-5 h-5 text-xs font-bold text-white bg-red-500 border-2 border-white rounded-full -top-2 -right-2 dark:border-gray-900">
              {notifications.length - 1}
            </div>
          </div>
        ) : (
          <div className="relative w-auto cursor-pointer">
            <Bell className="h-7" />
          </div>
        )}
      </MenuHandler>
      <MenuList className="w-96">
        {/* TODO: 알림 어떻게 바뀌는지에 따라 모름 */}
        {notifications.length > 1 ? (
          notifications.slice(0, -1).map((notification, index) => (
            <MenuItem
              key={index}
              className="flex items-center gap-2 border-none bg-none hover:outline-none hover:border-none"
            >
              <div>{notification.title}</div>
              {/* <div>{notification.content}</div> */}
            </MenuItem>
          ))
        ) : (
          <MenuItem className="flex justify-center border-none cursor-default bg-none hover:outline-none hover:border-none">
            알림이 없습니다.
          </MenuItem>
        )}
      </MenuList>
    </Menu>
  );
};

export default MatchingNotification;
