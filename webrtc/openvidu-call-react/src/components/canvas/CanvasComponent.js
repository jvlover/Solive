import React, {useEffect, useRef, useState} from 'react';

const img = new Image();
img.src = "../../assets/images/SoliveW.png";
const Canvas = () => {
    const canvasRef = useRef(null);
    const contextRef = useRef(null);

    const [ctx, setCtx] = useState(); // 캔버스의 드로잉 컨텍스트
    const [isDrawing, setIsDrawing] = useState(false);

    useEffect(() => {
        const canvas = canvasRef.current;
        canvas.width = window.innerWidth * 0.5;
        canvas.height = window.innerHeight;

        const context = canvas.getContext("2d");
        context.strokeStyle = "black";
        context.lineWidth = 2.5;
        contextRef.current = context;
        context.drawImage(img, 0, 0, canvas.width, canvas.height);
        setCtx(context);
    }, []);

    const startDrawing = () => {
        setIsDrawing(true);
    }

    const finishDrawing = () => {
        setIsDrawing(false);
    }

    const drawing = ({nativeEvent}) => {
        const {offsetX, offsetY} = nativeEvent;
        // canvas.getContext('2d')의 값이 있을 때
        if (ctx) {
            if (!isDrawing) {
                ctx.beginPath();
                ctx.moveTo(offsetX, offsetY);
            } else {
                ctx.lineTo(offsetX, offsetY);
                ctx.stroke();
            }
        }
    };

    return (
        <div className="canvas_wrap">
            <canvas
                ref={canvasRef}
                onMouseDown={startDrawing}
                onMouseUp={finishDrawing}
                onMouseMove={drawing}
                onMouseLeave={finishDrawing}
            ></canvas>
        </div>
    )
}
export default Canvas;