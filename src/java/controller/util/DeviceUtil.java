//package controller.util;
//
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.Date;
//import javax.faces.context.FacesContext;
//import javax.servlet.http.HttpServletRequest;
//import net.sf.uadetector.ReadableUserAgent;
//import net.sf.uadetector.UserAgentStringParser;
//import net.sf.uadetector.service.UADetectorServiceFactory;
//
//public class DeviceUtil {
//
//    private static final DeviceUtil instance = new DeviceUtil();
//
//    public static DeviceUtil getInstance() {
//        return instance;
//    }
//
//    private DeviceUtil() {
//    }
//
//    private static ReadableUserAgent getUserAgent() {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
//        ReadableUserAgent agent = parser.parse(httpServletRequest.getHeader("User-Agent"));
//        return agent;
//    }
//
//    public static Device getDevice() {
//        ReadableUserAgent ag = getUserAgent();
//        Device device = new Device();
//        device.setNavigateur(ag.getFamily().getName());
//        device.setOs(ag.getOperatingSystem().getName());
//        device.setDeviceCategorie(ag.getDeviceCategory().getName());
//        device.setDateConnection(new Date());
//        InetAddress ip;
//        try {
//
//            ip = InetAddress.getLocalHost();
//            device.setIp(ip.getHostAddress());
//
////            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
////
////            byte[] mac = network.getHardwareAddress();
////            StringBuilder sb = new StringBuilder();
////            for (int i = 0; i < mac.length; i++) {
////                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
////            }
////            device.setMacAdresss(sb.toString());
//
//        } catch (UnknownHostException e) {
//
//            e.printStackTrace();
//
//        }
//        return device;
//    }
//    public static Device getManagerDevice(Manager manager) {
//         Device device = getDevice();
//         device.setManager(manager);
//         return device;
//     }
//    
//    public static Device getWorkerDevice(Worker worker) {
//         Device device = getDevice();
//         device.setWorker(worker);
//         return device;
//     }
//}