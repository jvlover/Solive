import NotFoundImage from '../assets/404.png';

const Error404 = () => {
  return (
    <div className="flex flex-col items-center justify-center h-screen">
      <img
        src={NotFoundImage}
        alt="Page not found"
        className="w-1/4 h-auto object-contain"
      />
      <h1 className="mt-20">404 not found</h1>
    </div>
  );
};

export default Error404;
