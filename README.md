一：代码说明：
1.1代码 

由于自动化脚本由java编写，所以在执行前需先安装java，☞点我查看安装教程








1.3 如何启动与配置
1.3.1, 全局url配置：

clone下代码后，等待maven加载依赖，然后在applicatiom.yaml文件中根据运行环境注释与放开对应地址执行即可

1.3.2 数据库配置

由于香港uat与测试环境数据共享， 所以理论上在香港uat运行的数据，在测试环境也能查看，这里偷懒没有修改db地址，如果后续需要切换成香港uat的数据库，同样在application.yaml中修改cornerstone下对应的db配置即可

url：数据库连接url

username: 登录数据库用户名

password: 登录数据库密码

driver-class-name: 数据库连接驱动（一般情况无需更改）

1.3.3 启动

一般情况下如果使用Idea，会默认加载此启动类，若没有，需手动添加 ，执行步骤如下：

在右上角点击运行框，下拉找到Edit Configuation

弹框点击+，下拉框找到Springboot

配置启动 的java版本与主启动类，启动类在主文件下（com.zane.AddressSortingApplication.class），添加完后，在执行栏中点击第二个图标，以debug模式运行。

观察控制台日志，出现红框的文字说明启动成功。

二：使用说明
2.1 支持场景：

目前地址分拣支持的场景为：LM下单、订单签收、订单改单（地址修正、地址收集）

2.2使用方式：

服务端成功启动后，接口调用即可，在进行调用时根据不同的传参会进行不同的分拣，具体使用如下：

2.2.1 接口入参：
{
    "sortingType": 2,
    "consigneeAddress": "Saint Alcalmiya Hospital",
    "consigneeArea":"ZaneX",
    "consigneeCountry":"NZL",
    "consigneePhone":"+76867989",
    "consigneeCity":"ZaneC",
    "consigneeProvince":"ZaneWeP",
    "consigneeZipCode":"6021",
    // "latitude":"-41.28979770181855",
    // "longitude":"174.78702568271694",
    "waybillNo":"{{$timestamp}}",
    "consignee":"分拣人",
    "orderType":"100",
    //签收坐标，签收分拣必填
    "deliverLongitude":174.79702568271694,
    "deliverLatitude":-41.28979770181855，
	"userCode":"2103471201",
    "password":"19283746test!@#" }
2.2.2 参数说明：
参数名	说明	备注
sortingType	分拣方式——采用哪种方式进行分拣回归，0：下单、1：签收、2：改单	










必填，省市区以及详细地址均为需要参与分拣的具体省市区地址信息，


consigneeAddress	订单收件人详细地址
consigneeArea	订单收件人地址所属区域
consigneeCountry	订单收件人所属国家
consigneePhone	订单收件人电话
consigneeCity	订单收件人所在城市
consigneeProvince	订单收件人所在省
consigneeZipCode	订单收件人所在区域的zipCode	视分拣配置进行填写，若国家采用ZipCode进行区域划分，则必填（KSA,UAE等中东国家采用网格分拣，可不填；MEX,BRA等南美国家必填）
latitude	订单收件人纬度	

如果从谷歌地图拿经纬度的话需要调换一下顺序，因为谷歌地图是以纬经度返回，并非经纬度


longitude	订单收件人经度
waybillNo	订单号	

必填（每次调用保证不同即可，建议用随机字符串代替）


consignee	收件人姓名	

非必填


orderType	订单类型——100：LM订单（配送订单），1200：FM订单（直提直送订单）	

必填


deliverLongitude	订单签收坐标-经度	

如果要进行签收分拣，则这两个字段必填，其他情况非必填





deliverLatitude	订单签收坐标-纬度
userCode	登录系统的用户名（测试环境TMS系统登录账号）	

必填，否则会提示登录无效





password	登录的用户名对应的密码
2.2.3 接口出参

分拣结果将分拣记录表（cs_adderss_sorting_record）、坐标获取记录表（cs_address_match_point_record）、回写记录表（cs_address_sorting_write_record）聚合展示，根据分拣方式的不同，返回的结果也不同，

{
    "msg": "true",
    "addressCode": "297bd520f0a2ace28e79bfcbed2a1b01",
    "orderNo": "1723532087",
    "sortStrategy": "V4_POINT_AREA_GEOFENCE",
    "sortedStationCode": "S2101391501",
    "trustValue": "80.00",
    "writeBackInfo": {
        "id": 1247255687975985152,
        "isAreaReturn": true,
        "isCityReturn": false,
        "isFirstReturn": false,
        "isFourReturn": false,
        "isPointReturn": true,
        "isProvinceReturn": false,
        "isSecondReturn": false,
        "isStationReturn": true,
        "isThirdReturn": false,
        "isZipCodeReturn": false,
        "isZoneReturn": true,
        "orgId": 10,
        "result": "",
        "sortRecordId": 1247255687829184513,
        "waybillNo": "1723532087"
    },
    "sortResult": "success",
    "pointFetchWay": "13",
    "原始坐标获取方式": "自带经纬度",
    "sortedGeofenceCode": "G280762",
    "sortType": "ORDER_CHANGE",
    "matchPointResult": 1,
    "writeBackJson": {
        "addressCode": "297bd520f0a2ace28e79bfcbed2a1b01",
        "addressId": "1247206325895880704",
        "belongsGeofenceCode": "G280762",
        "latitude": "-41.289798",
        "longitude": "174.787026",
        "newArea": "ZaneA",
        "newZone": "CESHI",
        "orgId": 10,
        "originalPointSource": "1",
        "pointSource": "STANDARD_POINT_MATCH",
        "stationCode": "S2101391501",
        "stationName": "ZANEDS",
        "status": 1,
        "timeStamp": "1723532088016",
        "trustValue": "83",
        "type": 2,
        "waybillNo": "1723532087"
    },
    "pointFetchWayDesc": "标准地址库匹配",
    "geocode": "POINT(174.787026 -41.289798)",
    "inputStationCode": "S2101391501"
}

参数说明：

参数名	说明	备注
sortedGeofenceCode	分拣后的电子围栏编码	网格分拣下返回网格编码，UZ分拣下返回多边形简码


msg

	任务执行消息，执行成功时为“success”，	仅代表当前任务的执行情况，不代表实际分拣结果，实际分拣结果参考sortResult字段


addressCode

	地址编码——省市区详细地址使用算法生成的code，用于个人地址库、标准地址库等检索使用	

orderNo	订单号	

sortStrategy	分拣策略——根据当前国家的分拣配置来决定采取的分拣方式	具体分拣方式枚举见后
sortedStationCode	分拣后的网点简码	

trustValue	地址信任分值	

writeBackInfo	当前运单的回写信息	

包含回写相关的配置数据：三段码是否返回、省市区是否返回等字段


sortResult	当前运单的实际分拣结果，success表示分拣成功	

pointFetchWay	当前运单的坐标匹配方式枚举值，值为-1表示坐标匹配失败	

具体获取枚举见后，坐标匹配方式指当前运单拿到坐标的方式（前提是有坐标）；做包获取方式指这个坐标是通过什么方式拿到的，和运单关联性不强。




原始坐标获取方式

	运单当前坐标的获的方式


sortType

	分拣方式，目前支持上面提到的三种方式，下单、签收、改单	

ORDE_CHANGE：改单、PRE_ORDER_CREATE：下单、DELIVERED_POINT：订单签收




matchPointResult

	坐标匹配结果，1表示坐标匹配成功	签收时不进行坐标匹配，所以订单签收的分拣方式下无坐标匹配结果


writeBackJson

	当前运单回写记录发送给mq的消息体	回写消息发送给mq的消息格式
pointFetchWayDesc	当前运单的坐标匹配方式	pointFetchWay是枚举值，这个是枚举对应的详细type，如果不知道具体的匹配枚举值，可以看这个


geocode

	分拣后的运单坐标	展示结果样例：POINT(174.797026 -41.289798)，括号内为经纬度
inputStationCode	下单时传递的网点	


附1：分拣策略枚举值

/**
 * 分拣策略（细分，即主策略下的小版本）
 *
 * @author Noah
 * @email noah.xie@imile.me
 * @date 2022/8/8
 */
@Getter
public enum SortingStrategyDetailVersionEnum {

    /**
     * V3.1版本-坐标区域电子围栏分拣,与网格管理分拣结合使用
     */
    V3_1_POINT_AREA_GEOFENCE(SortingStrategyEnum.POINT_AREA_GEOFENCE),
    /**
     * V3.2版本-坐标区域电子围栏分拣,与网格管理分拣结合使用,且优先信任个人地址簿信任分值大于40分的数据
     */
    V4_POINT_AREA_GEOFENCE(SortingStrategyEnum.POINT_AREA_GEOFENCE),
    /**
     * V3版本-坐标zipcode电子围栏分拣
     */
    V3_POINT_ZIP_CODE_GEOFENCE(SortingStrategyEnum.POINT_ZIP_CODE_GEOFENCE),
    /**
     * V4版本-坐标zipcode电子围栏分拣
     */
    V4_POINT_ZIP_CODE_GEOFENCE(SortingStrategyEnum.POINT_ZIP_CODE_GEOFENCE),
    /**
     * V3版本-关键词分拣
     */
    V3_KEYWORDS(SortingStrategyEnum.KEYWORDS),
    /**
     * V4版本-关键词分拣
     */
    V4_KEYWORDS(SortingStrategyEnum.KEYWORDS),
    /**
     * V3版本-区域纠正历史记录分拣
     */
    V3_AREA_CORRECT_HISTORY(SortingStrategyEnum.AREA_CORRECT_HISTORY),
    /**
     * V4版本-区域纠正历史记录分拣
     */
    V4_AREA_CORRECT_HISTORY(SortingStrategyEnum.AREA_CORRECT_HISTORY),
    /**
     * V3_详细地址分拣
     */
    V3_AREA_REGION(SortingStrategyEnum.AREA_REGION),
    /**
     * V4-详细地址分拣
     */
    V4_AREA_REGION(SortingStrategyEnum.AREA_REGION),
    /**
     * V4版本-网格管理分拣
     */
    V4_ZONE_MANAGEMENT(SortingStrategyEnum.ZONE_MANAGEMENT),
    /**
     * V4版本-详细地址邮编分拣
     */
    V4_DETAIL_ADDRESS_ZIP_CODE(SortingStrategyEnum.DETAIL_ADDRESS_ZIP_CODE),
    /**
     * V4版本-UZ电子围栏分拣
     */
    V4_POINT_UZ_GEOFENCE(SortingStrategyEnum.POINT_UZ_GEOFENCE),
    /**
     * V4版本-国家电子围栏分拣
     */
    V4_POINT_COUNTRY_GEOFENCE(SortingStrategyEnum.POINT_COUNTRY_GEOFENCE),
    ;
    /**
     * 所属分拣策略
     */
    private SortingStrategyEnum belongSortingStrategy;

    SortingStrategyDetailVersionEnum(SortingStrategyEnum belongSortingStrategy) {
        this.belongSortingStrategy = belongSortingStrategy;
    }

    public static SortingStrategyDetailVersionEnum getInstance(String name) {
        if (name == null) {
            return null;
        }
        for (SortingStrategyDetailVersionEnum value : SortingStrategyDetailVersionEnum.values()) {
            if (name.equals(value.name())) {
                return value;
            }
        }
        return null;
    }
}



附2：坐标匹配方式&坐标获取方式

USER_DEFINITION(1, "用户下单传入的经纬度"),
    USER_ADDRESS_TERM(2, "精准匹配历史用户地址"),
    KEY_WORD_TERM(3, "命中关键词"),
    STANDARD_ADDRESS_MATCH(4, "官方地址模糊匹配"),
    USER_ADDRESS_MATCH(5, "用户地址模糊匹配"),
    GOOGLE_MAPS(6, "调用谷歌获取"),
    HISTORY_REASON(7, "导入历史数据"),
    TEXT_PARSE_POINT(8, "用户地址文本解析出经纬度"),
    USER_APPOINTMENT(9, "用户预约"),
    WHATS_APP(10, "whatsApp"),
    DELIVERYED(11, "用户签收"),  //签收也是一种分拣方式，仅在消息的时候使用

    USER_INFO_MATCH(12, "个人地址簿经纬度匹配"),
    STANDARD_POINT_MATCH(13, "标准地址经纬度匹配"),
    EXTERNAL_ADDRESS_MATCH(14, "外部地址经纬度匹配"),
    /**
     * MWEB 用户改单（用户地址收集）
     */
    USER_CHANGE_ADDRESS(15, "用户地址修改"),
    DELIVERED_POINT_TRANSLATE(16, "坐标签收转译"),
    DELIVERED_POINT_TRANSLATE_HERE(17, "签收坐标Here转译"),
    /**
     * 签收坐标转译(通过NEXT_BILLION地图形式)
     */
    DELIVERED_POINT_TRANSLATE_NEXT_BILLION(18, "签收坐标NextBillion转译"),

    /**
     * 司机定位地址，仅落标准地址库
     */
    DRIVER_LOCATION(19, "司机定位"),
    /**
     * 下单地址用户反馈
     */
    ORDER_CREATE_USER_FEEDBACK(20, "下单地址用户反馈"),
    /**
     * 墨西哥地址导入，仅落标准地址库
     */
    MEX_ADDRESS_IMPORT(21, "墨西哥地址导入"),

    ORDER_CHANGE(22, "地址修改"),
    /**
     * 系统预约(客服预约、NDR审核等非用户渠道的预约)
     */
    SYSTEM_SCHEDULE(23, "系统预约"),
    /**
     * 地址修正
     */
    ORDER_ADDRESS_FIX(24, "地址修正"),
    /**
     * NDR派件审核通过
     */
    NDR_DELIVERY_AUDIT_PASS(25, "NDR派件审核通过"),
    /**
     * 订单确认
     */
    ORDER_CONFIRM(26, "订单确认(C2C 客户确认)"),
    /**
     * 系统订单确认
     */
    SYS_ORDER_CONFIRM(27, "订单确认(系统确认)"),
    /**
     * 根据规则定义
     */
    RULE_DEFINITION(28, "根据规则定义"),

    NEXT_BILLION_MAPS(29, "调用nb获取"),

    HERE_MAPS(30, "调用here获取"),
    /**
     * 签收成功时，保存翻译后的地址
     */
    DELIVERED_TRANSLATE(31, "签收成功翻译后地址"),
    /**
     * 下单翻译后地址
     */
    USER_DEFINITION_TRANSLATE(32, "下单翻译后地址"),
    /**
     * 卖家地址初始化
     */
    SELLER_ADDRESS_IMPORT(33, "导入卖家地址"),

    /**
     * 卖家地址初始化
     */
    PiCK_UP_SELLER_ADDRESS(34, "fm订单取件扫描地址"),
    PLUS_CODE(35, "PlusCode解析"),
    /**
     * KSA短地址解析
     */
    SHORT_ADDRESS(36, "KSA短地址解析"),

    FM_ROUTE(37,"fm路径规划订单搜索地址"),
    /**
     * 地址纠偏
     */
    ADDRESS_FIX(38, "地址纠偏界面进行修改"),
	/**
     * 卖家地址库
     */
    ADDRESS_FIX(39, "卖家地址库"),
	/**
     * 地图服务商
     */
    ADDRESS_FIX(40, "地图服务商")
2.3 逻辑实操
2.3.1 正向逻辑

目前生产环境进行回归的国家暂定为CHN和PAK，CHN计划作为UZ分拣使用，PAK计划作为网格分拣使用，在行政区信息以及网格等地图信息维护好的前提下，要进行正向结果的自动化回归，只需传入已经维护好的网格所属的省市区或UZ图层内的坐标即可，注意传参时切换sortignType来进行不同场景下的分拣；如果分拣成功，分拣结果为success；若同时坐标获取成功，则matchPointResult为1

2.3.2 异常逻辑

1、系统异常：若因为某些原因（系统原因、数据维护异常）等造成的接口调用失败，会提示系统报错消息，并返回traceId，可以拿着traceId查看日志进一步排查异常原因

2、分拣失败的场景（比如UZ分拣中坐标没维护图层，或网格分拣中坐标没命中网格），会提示分拣失败，但是会返回当前坐标的获取方式以及坐标，可以拿着坐标进一步去电子地图验证坐标的落点是否正常


