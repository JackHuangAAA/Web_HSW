package com.ethink.lineup.plugin;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ethink.lineup.Const;
import com.ethink.lineup.controller.USBDeviceFace;
import com.ethink.lineup.controller.UsbConnectUtil;
import com.ethink.lineup.service.api.RxManager;
import com.ethink.plugin.BasePlugin;
import com.ethink.plugin.message.EventMessage;
import com.ethink.tools.serialport.usb.util.HexDump;


import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 * 扫码枪插件
 */
public class USBScannerPlugin extends BasePlugin implements Runnable, USBDeviceFace {
    private static final String TAG = "USBScannerPlugin";
    private final RxManager rxManager;
    private Context context;
    private Thread thread;
    private UsbConnectUtil usbController;

    private List<UsbEndpoint> mReadEndpoint1 = new ArrayList<>();
    private UsbEndpoint mReadEndpoint;
    private UsbEndpoint mWriteEndpoint;
    /**
     * Internal read buffer.  Guarded by {@link #mReadBufferLock}.
     */
    protected byte[] mReadBuffer;

    /**
     * Internal write buffer.  Guarded by {@link #mWriteBufferLock}.
     */
    protected byte[] mWriteBuffer;

    protected final Object mReadBufferLock = new Object();
    protected final Object mWriteBufferLock = new Object();
    public static final int DEFAULT_READ_BUFFER_SIZE = 16 * 1024;
    public static final int DEFAULT_WRITE_BUFFER_SIZE = 16 * 1024;
    private UsbDeviceConnection connection;
    private MessageToast messageToast;

    public USBScannerPlugin(Context context) {
        super("SCANNER");
        this.context = context;
        messageToast = new MessageToast(context);
        rxManager = new RxManager();
        mReadBuffer = new byte[DEFAULT_READ_BUFFER_SIZE];
        mWriteBuffer = new byte[DEFAULT_WRITE_BUFFER_SIZE];
        EventBus.getDefault().register(this);
        usbController = new UsbConnectUtil(context, this);
        usbController.requestUsb();

    }


    /**
     * 读取扫码枪数据
     */
    @Override
    public void run() {
        //     byte[]b=new byte[]{0x16,0x54,0x0D,0x21};
        logger.info("run----------------------------");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (!Thread.interrupted() && connection != null && mReadEndpoint != null) {
            try {
                byte[] src = new byte[256];
                controlOut(9,0x0201,0); //触发中断上送数据
                int len = read(src, 200);
                if(len > 0) {
                    logger.info("read: {} len: {}", HexDump.toHexString(src, 0, len), len);
                    logger.info("buffer {} {} ",buffer.capacity(),buffer.position());
                    buffer.put(src,6,len-6);
                }else{
                    buffer.flip();
                    if(buffer.limit() > 0){
                        String tmp = new String(buffer.array(),0,buffer.limit()).replaceAll("\\u0000","");
                        //Log.d(TAG, "run: 扫码结果："+scantext);
                        logger.info("扫码数据 {} len: {}",tmp,tmp.length());
                        EventMessage eventMessage = new EventMessage("SCANNER_RESULT");
                        eventMessage.setString("data", tmp);
                        pluginManager.post(eventMessage);
                      //  messageToast.obtainMessage(1, tmp).sendToTarget();
                        EventBus.getDefault().post(eventMessage, Const.SCAN_EVENT);
                    }
                    buffer.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("读取usb失败", e.getMessage());
                //EventBus.getDefault().post(new WriteConsoleEvent("读串口超时 " + e.getMessage()));
            }
        }
    }

    @Override
    public void onStart()     {

    }


    @Override
    public void onStop() {
        if (connection != null) {
            connection.close();
        }
        thread.interrupt();
    }

    @Override
    public boolean checkUSB(UsbDevice usbDevice) {
        return usbDevice.getVendorId() == 1504 && usbDevice.getProductId() == 6400;
    }

    @Override
    public void openUSB(UsbDevice usbDevice) {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        connection = usbManager.openDevice(usbDevice);
        for (int i = 0; i < usbDevice.getInterfaceCount(); i++) {
            UsbInterface usbIface = usbDevice.getInterface(i);
            logger.info("usbIface {} {}", i, usbIface.toString());
            if (connection.claimInterface(usbIface, true)) {
                Log.d(TAG, "claimInterface " + i + " SUCCESS");
            } else {
                Log.d(TAG, "claimInterface " + i + " FAIL");
            }
        }
        for (int j = 0; j < usbDevice.getInterfaceCount(); j++) {
            UsbInterface dataIface = usbDevice.getInterface(j);
            for (int i = 0; i < dataIface.getEndpointCount(); i++) {
                UsbEndpoint ep = dataIface.getEndpoint(i);
                logger.info("UsbEndpoint " + ep.toString() + " " + ep.getDirection() + " " + ep.getType());
                if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                    if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                        mWriteEndpoint = ep;
                    }else{
                        mReadEndpoint1.add(ep);
                    }
                } else if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_INT) {
                    if (ep.getDirection() == UsbConstants.USB_DIR_IN) {
                        mReadEndpoint = ep;
                    }
                }
            }
        }
        thread = new Thread(this);
        thread.start();
        logger.info("开启线程扫码----------------");
    }

    @Override
    public void deniedPermission() {
         Toast.makeText(context,"授权被拒绝，无法使用USB设备",Toast.LENGTH_LONG).show();
    }


    public int write(byte[] src, int timeoutMillis) throws IOException {
        int offset = 0;
        while (offset < src.length) {
            final int writeLength;
            final int amtWritten;
            synchronized (mWriteBufferLock) {
                final byte[] writeBuffer;

                writeLength = Math.min(src.length - offset, mWriteBuffer.length);
                if (offset == 0) {
                    writeBuffer = src;
                } else {
                    // bulkTransfer does not support offsets, make a copy.
                    System.arraycopy(src, offset, mWriteBuffer, 0, writeLength);
                    writeBuffer = mWriteBuffer;
                }
                amtWritten = connection.bulkTransfer(mWriteEndpoint, writeBuffer, writeLength, timeoutMillis);
            }
            if (amtWritten <= 0) {
                throw new IOException("Error writing " + writeLength
                        + " bytes at offset " + offset + " length=" + src.length);
            }

            Log.d(TAG, "Wrote amt=" + amtWritten + " attempted=" + writeLength);
            offset += amtWritten;
        }
        return offset;
    }

    public int read(byte[] dest, int timeoutMillis) {
        final int numBytesRead;
        synchronized (mReadBufferLock) {
            int readAmt = Math.min(dest.length, mReadBuffer.length);
            numBytesRead = connection.bulkTransfer(mReadEndpoint, mReadBuffer, readAmt,
                    timeoutMillis);
            if (numBytesRead < 0) {
                return 0;
            }
            System.arraycopy(mReadBuffer, 0, dest, 0, numBytesRead);
        }
        return numBytesRead;
    }

    private int controlOut(int request, int value, int index) {
        final int REQTYPE_HOST_TO_DEVICE = 0x21;
        return connection.controlTransfer(REQTYPE_HOST_TO_DEVICE, request,
                value, index, new byte[]{0x01,0x22,0x01,0x000}, 4, 5000);
    }

    public static class MessageToast extends Handler {
        private WeakReference<Context> weakReference;

        MessageToast(Context context) {
            weakReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Context context = weakReference.get();
            if (msg.what == 1 && context != null) {
                Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }


}


