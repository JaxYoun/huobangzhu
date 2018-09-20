import { MyApp } from './app.component';
import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { SplashScreen } from '@ionic-native/splash-screen';
import { StatusBar } from '@ionic-native/status-bar';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { HttpModule} from '@angular/http';
// import { Geolocation } from '@ionic-native/geolocation';
import { Camera,FilePath } from 'ionic-native';
import { FileTransfer} from '@ionic-native/file-transfer';
import { File } from '@ionic-native/file';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { PublicHeader } from '../pages/components/header';
import { TabsPage } from '../pages/tabs/tabs';
import { HomePage } from '../pages/home/home';
import { WelcomePage } from '../pages/welcome/welcome';
import { ShipperPage } from '../pages/shipper/shipper';
import { LoginPage } from '../pages/login/login';
import { RegisterPage } from '../pages/login/register';
import { RegisterSubPage } from '../pages/login/register_sub'
import { ForgetPage } from '../pages/login/forget';
import { ForgetSubPage } from '../pages/login/forget_sub'
import { ProtocolPage } from '../pages/protocol/protocol'
import { ObjNgFor } from '../pages/components/keys.pipe'
import { Fulltransport } from '../pages/shipper/fulltransport/index'
import { FulltransportModals } from '../pages/shipper/fulltransport/modals'
import { ConfirmOrder } from "../pages/shipper/fulltransport/confirmOrder";
import { PayMonthly } from '../pages/shipper/fulltransport/payMonthly'
import { PayOnline } from '../pages/shipper/fulltransport/payOnline';
import { CarCollect } from '../pages/shipper/fulltransport/carCollect';
import { CarCollectDetail } from '../pages/shipper/fulltransport/carCollectDetail';
import { OrderDetail } from '../pages/shipper/fulltransport/orderDetail';
import { SendList } from '../pages/person/sendOrder/sendList';
import { SendOrderDetail } from '../pages/person/sendOrder/sendOrderDetail';
import { JoinList } from '../pages/person/takeOrder/joinList';
import { HelpMeBuy } from '../pages/shipper/helpMeBuy/helpMeBuy'
import { HelpMeBuyModals } from '../pages/shipper/helpMeBuy/modals'
import { HelpMeBuyConfirmOrder } from '../pages/shipper/helpMeBuy/confirmOrder'
import { HelpMeBuyMapModals } from '../pages/shipper/helpMeBuy/mapModals'
import { HelpMeBuyTake } from '../pages/driver/helpMeBuy/helpMeBuyTake'
import { helpMeBuyTakeConfirmOrder } from '../pages/driver/helpMeBuy/confirmOrder';
import { HelpMeSendTake } from '../pages/driver/helpMeSend/helpMeSendTake'
import { HelpMeSendTakeConfirmOrder } from '../pages/driver/helpMeSend/confirmOrder';
import { HelpMeSend } from '../pages/shipper/helpMeSend/helpMeSend'
import { HelpMeSendConfirmOrder } from '../pages/shipper/helpMeSend/confirmOrder'
import { IonicStorageModule } from '@ionic/storage';
import { UserService } from './services/user.service';
import { WechatService } from './services/wechat.service';
import { HelperService } from './services/help.service';
import { FileUploadService } from './services/fileUpload.service';
// import { BackgroundMode } from '@ionic-native/background-mode';
import { LocationProvider } from '../providers/LocationProvider';
import { Config } from '../app/config';
import { SpecialLine } from '../pages/driver/specialLine/specialLine';
import { SpecialLineModals } from '../pages/driver/specialLine/modals';
import { SelectCarByOrders } from "../pages/shipper/selectCarByOrders";
import { FindGoods } from "../pages/driver/findGoods/findGoods";
import { FindGoodsMap } from "../pages/driver/findGoods/findGoodsMap";
import { SelectCarDriver } from "../pages/driver/selectCar/index";
import { SelectCarDetail } from "../pages/driver/selectCar/selectCarDetail";
import { PersonCenter } from "../pages/person/personCenter";
import { Person } from "../pages/person/auth/person";
import { PersonSetting } from '../pages/person/personSetting';
import { Driver } from "../pages/driver/driver";
import { FullCarry } from "../pages/driver/fulltransport/fullCarry";
import { FullCarryMap } from "../pages/driver/fulltransport/fullCarryMap";
import { FindCarDetail } from "../pages/shipper/findCarDetail";
import { PersonShipperAuth } from "../pages/person/auth/personShipperAuth";
import { PersonDriverAuth } from "../pages/person/auth/personDriverAuth";
import { ShipperCompanyAuth } from '../pages/person/auth/shipperCompanyAuth';
import { TransportCompanyAuth } from '../pages/person/auth/transportCompanyAuth';
import { DeliveryBoyAuth } from '../pages/person/auth/deliveryBoyAuth';
import { TakeList } from '../pages/person/takeOrder/takeList';
import { TakeOrderDetail } from '../pages/person/takeOrder/takeOrderDetail';
import { TakeSign } from '../pages/person/takeOrder/takeSign';
import {FindCar} from '../pages/shipper/findCar';
import { WaitToTake } from "../pages/shipper/waitToTake";
import { OrderToReceive } from "../pages/shipper/orderToReceive";
import { SelectDriver } from "../pages/shipper/selectDriver";
import { FindCars } from '../pages/shipper/findCar/findCars';
import { StarLevel } from "../pages/components/stars";
import {SendGoods} from '../pages/shipper/sendGoods/index';
import {AddressList} from '../pages/shipper/sendGoods/addressList';
import {SelectGoodsModal} from '../pages/shipper/sendGoods/selectGoodsModal';
import {LinkModal} from '../pages/shipper/sendGoods/linkModal';
import { AppHeader } from '../pages/components/appHeader';
import { Warehouse } from '../pages/warehouse/warehouse/warehouse';
import { WarehouseDetail } from '../pages/warehouse/warehouse/warehouseDetail';
import { InformationDetail } from '../pages/warehouse/information/informationDetail';
import { Shop } from '../pages/person/shop/index';
import { ProductDetail } from '../pages/person/shop/productDetail';
import { ProductOfType } from '../pages/person/shop/productOfType';
import { Wallet } from '../pages/person/wallet/wallet';
import { BondPay } from '../pages/person/wallet/bondpay';
import { BondRefund } from '../pages/person/wallet/bondrefund';
import { CityPicker } from  "../pages/components/cityPicker";
// import { CityPickerService } from  '../providers/city.picker.service';
import { BackButtonService } from  '../providers/BackButtonService';
import { AuthPage } from '../pages/person/auth/auth';
import { EarnestMoney } from '../pages/warehouse/warehouse/earnestMoney';
import { Score } from '../pages/person/score/score';
import { MessagePage } from '../pages/person/message/message';
import { AppRefresh } from '../pages/components/appRefresh';
import { LeaseList } from '../pages/person/leaseOrder/leaseList';
import { LeaseOrderDetail } from '../pages/person/leaseOrder/leaseOrderDetail';
import { CommentOrder } from '../pages/person/commentOrder/commentOrder';
import { DriverComment } from '../pages/person/commentOrder/driverComment';
import { SubmitComment } from '../pages/person/commentOrder/submitComment';
import { MyWarehouse } from '../pages/person/myWarehouse/myWarehouse';
import { MyWarehouseEdit } from '../pages/person/myWarehouse/myWarehouseEdit';
import { MyWarehouseDetail } from '../pages/person/myWarehouse/myWarehouseDetail';
import { SystemConfig } from '../pages/person/systemConfig/systemConfig';
import { ToggleCardDirective } from '../pages/components/toggleCard.directive';
import { FulltransportAddress } from '../pages/person/oftenAddress/fulltransportAddress';
import { FullModals } from '../pages/person/oftenAddress/modals';
import { HelpAddress } from '../pages/person/oftenAddress/helpAddress';
import { HelpModals } from '../pages/person/oftenAddress/helpModals';
import { CarInfo } from '../pages/person/carInfo/carInfo';
import { CarInfoList } from '../pages/person/carInfo/carInfoList';
import { FindCarView } from '../pages/shipper/findCar/findCarView';
import { SelectPayWayModal } from '../pages/person/sendOrder/selectPayWayModal';

// import { WechatPay } from '../pages/person/test2/test2';
import { ContractList } from '../pages/person/contract/contractList';
import { ContractDetail } from '../pages/person/contract/contractDetail';
import {AppVersion} from '@ionic-native/app-version';
import {FileTransferObject} from '@ionic-native/file-transfer';
import {FileOpener} from '@ionic-native/file-opener';
import { SelectRefund } from '../pages/person/wallet/selectRefund';
import { CityPickerContent } from '../pages/components/cityPickerContent';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}


@NgModule({
  declarations: [
    MyApp,
    PublicHeader,
    TabsPage,
    HomePage,
    ShipperPage,
    WelcomePage,
    LoginPage,
    RegisterPage,
    RegisterSubPage,
    ForgetPage,
    ForgetSubPage,
    ProtocolPage,
    Fulltransport,
    FulltransportModals,
    HelpMeBuy,
    HelpMeBuyModals,
    HelpMeBuyConfirmOrder,
    HelpMeBuyMapModals,
    HelpMeBuyTake,
    helpMeBuyTakeConfirmOrder,
    HelpMeSendTake,
    HelpMeSendTakeConfirmOrder,
    HelpMeSend,
    HelpMeSendConfirmOrder,
    ObjNgFor,
    ConfirmOrder,
    PayMonthly,
    PayOnline,
    CarCollect,
    CarCollectDetail,
    OrderDetail,
    SendList,
    SendOrderDetail,
    JoinList,
    SpecialLine,
    SpecialLineModals,
    SelectCarByOrders,
    FindGoods,
    FindGoodsMap,
    SelectCarDriver,
    SelectCarDetail,
    PersonCenter,
    PersonSetting,
    Person,
    Driver,
    FullCarry,
    FullCarryMap,
    PersonShipperAuth,
    PersonDriverAuth,
    ShipperCompanyAuth,
    TransportCompanyAuth,
    DeliveryBoyAuth,
    TakeList,
    TakeOrderDetail,
    TakeSign,
    FindCarDetail,
    FindCar,
    WaitToTake,
    OrderToReceive,
    SelectDriver,
    FindCars,
    StarLevel,
    SendGoods,
    AddressList,
    SelectGoodsModal,
    LinkModal,
    AppHeader,
    Warehouse,
    WarehouseDetail,
    InformationDetail,
    Shop,
    ProductDetail,
    ProductOfType,
    Wallet,
    BondPay,
    BondRefund,
    AuthPage,
    EarnestMoney,
    Score,
    MessagePage,
    AppRefresh,
    LeaseList,
    LeaseOrderDetail,
    CommentOrder,
    DriverComment,
    SubmitComment,
    SystemConfig,
    MyWarehouse,
    MyWarehouseEdit,
    MyWarehouseDetail,
    CityPicker,
    ToggleCardDirective,
    FulltransportAddress,
    FullModals,
    HelpAddress,
    HelpModals,
    CarInfo,
    CarInfoList,
    FindCarView,
    SelectPayWayModal,
    // WechatPay
    ContractList,
    ContractDetail,
    SelectRefund,
    CityPickerContent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    HttpModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    }),
    IonicModule.forRoot(MyApp),
    IonicStorageModule.forRoot(),
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    TabsPage,
    HomePage,
    ShipperPage,
    WelcomePage,
    LoginPage,
    RegisterPage,
    RegisterSubPage,
    ForgetPage,
    ForgetSubPage,
    ProtocolPage,
    Fulltransport,
    FulltransportModals,
    HelpMeBuy,
    HelpMeBuyModals,
    HelpMeBuyConfirmOrder,
    HelpMeBuyMapModals,
    HelpMeBuyTake,
    helpMeBuyTakeConfirmOrder,
    HelpMeSendTake,
    HelpMeSendTakeConfirmOrder,
    HelpMeSend,
    HelpMeSendConfirmOrder,
    ConfirmOrder,
    PayMonthly,
    PayOnline,
    CarCollect,
    CarCollectDetail,
    OrderDetail,
    SendList,
    SendOrderDetail,
    JoinList,
    SpecialLine,
    SpecialLineModals,
    SelectCarByOrders,
    FindGoods,
    FindGoodsMap,
    SelectCarDriver,
    SelectCarDetail,
    PersonCenter,
    PersonSetting,
    Person,
    Driver,
    FullCarry,
    FullCarryMap,
    PersonShipperAuth,
    PersonDriverAuth,
    ShipperCompanyAuth,
    TransportCompanyAuth,
    DeliveryBoyAuth,
    TakeList,
    TakeOrderDetail,
    TakeSign,
    FindCarDetail,
    FindCar,
    WaitToTake,
    OrderToReceive,
    SelectDriver,
    FindCars,
    StarLevel,
    SendGoods,
    AddressList,
    SelectGoodsModal,
    LinkModal,
    AppHeader,
    Warehouse,
    WarehouseDetail,
    InformationDetail,
    Shop,
    ProductDetail,
    ProductOfType,
    Wallet,
    BondPay,
    BondRefund,
    AuthPage,
    EarnestMoney,
    Score,
    MessagePage,
    AppRefresh,
    LeaseList,
    LeaseOrderDetail,
    CommentOrder,
    DriverComment,
    SubmitComment,
    SystemConfig,
    MyWarehouse,
    MyWarehouseEdit,
    MyWarehouseDetail,
    CityPicker,
    FulltransportAddress,
    FullModals,
    HelpAddress,
    HelpModals,
    CarInfo,
    CarInfoList,
    FindCarView,
    SelectPayWayModal,
    // WechatPay
    ContractList,
    ContractDetail,
    SelectRefund,
    CityPickerContent    
  ],
  providers: [
    StatusBar,
    SplashScreen,
    // Geolocation,
    Camera,File,FilePath,FileTransfer,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    UserService,
    BackButtonService,
    WechatService,
    HelperService,
    FileUploadService,
    // BackgroundMode,
    LocationProvider,
    AppVersion,
    FileOpener,
    FileTransferObject,
    Config
  ]
})
export class AppModule {}
