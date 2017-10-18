package com.cnsunway.saas.wash.model;

public class Store {

    String address ;//(string, optional): 门店地址 ,
    String addressDetail  ;//(string, optional): 详细地址 ,
    String beginService  ;//(LocalTime, optional): 营业开始时间 ,
    String capacity  ;//(integer, optional): 吞吐量 ,
    String createdBy  ;//(string, optional),
    String createdDate  ;//(string, optional),
    boolean deletedFlag  ;//(boolean, optional),
    String description  ;//(string, optional): 描述 ,
    String distance  ;//(number, optional),
    District district  ;//(District, optional),
    String districtId  ;//(integer, optional): 地区id ,
    String endService  ;//(LocalTime, optional): 营业结束时间 ,
    String freightAmount  ;//(number, optional): 运费 ,
    String freightInfo  ;//(string, optional),
    String freightRemitAmount  ;//(number, optional): 免运费最低价 ,
    String headPortraitUrl  ;//(string, optional): 门店图标 ,
    String id  ;//(integer, optional),
    String innerLevel  ;//(integer, optional): 星级 ,
    boolean isClosed  ;//(boolean, optional): 是否下线 ,
    boolean isTestStore ;//(boolean, optional): 是否测试门店:0-否,1-是 ,
    String latitude ;//(number, optional): 纬度 ,
    String longitude ;//(number, optional): 经度 ,
    int maxHangerSerial ;//(integer, optional): 最大挂衣号序号 ,
   int minHangerSerial;// (integer, optional): 最小挂衣号序号 ,
    int offset ;//(integer, optional),
    String orderBy ;//(string, optional),
    String outerLevel ;//(integer, optional): 评级 ,
    String ownerId ;//(integer, optional): 所有者ID ,
    String  page ;//(integer, optional),
    String polygonPoints ;//(string, optional): 服务范围的多边形点集合 ,
    String rechargeInfo ;//(string, optional),
   int rows ;//(integer, optional),
    String serviceCount ;//(integer, optional),
   int size ;//(integer, optional): 规模 ,
    String startAmount ;//(number, optional): 起价费 ,
   int status ;//(integer, optional): 状态:1-正常营业,2-暂停接单,3-暂停营业 ,
    String storeName ;//(string, optional): 门店店名 ,
    String  storeNickName ;//(string, optional): 门店别名 ,
    String  storeNo ;//(string, optional): 门店编号 ,
    String   storeTel ;//(string, optional): 门店座机号码 ,
    String   updatedBy ;//(string, optional),
    String  updatedDate ;//(string, optional),
   int version ;//(integer, optional),
    String  withdrawerBankAccount ;//(string, optional): 门店提现账户号 ,
    String  withdrawerName ;//(string, optional): 门店提现账户名
    boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getBeginService() {
        return beginService;
    }

    public void setBeginService(String beginService) {
        this.beginService = beginService;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(boolean deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getEndService() {
        return endService;
    }

    public void setEndService(String endService) {
        this.endService = endService;
    }

    public String getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(String freightAmount) {
        this.freightAmount = freightAmount;
    }

    public String getFreightInfo() {
        return freightInfo;
    }

    public void setFreightInfo(String freightInfo) {
        this.freightInfo = freightInfo;
    }

    public String getFreightRemitAmount() {
        return freightRemitAmount;
    }

    public void setFreightRemitAmount(String freightRemitAmount) {
        this.freightRemitAmount = freightRemitAmount;
    }

    public String getHeadPortraitUrl() {
        return headPortraitUrl;
    }

    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInnerLevel() {
        return innerLevel;
    }

    public void setInnerLevel(String innerLevel) {
        this.innerLevel = innerLevel;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public boolean isTestStore() {
        return isTestStore;
    }

    public void setTestStore(boolean testStore) {
        isTestStore = testStore;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getMaxHangerSerial() {
        return maxHangerSerial;
    }

    public void setMaxHangerSerial(int maxHangerSerial) {
        this.maxHangerSerial = maxHangerSerial;
    }

    public int getMinHangerSerial() {
        return minHangerSerial;
    }

    public void setMinHangerSerial(int minHangerSerial) {
        this.minHangerSerial = minHangerSerial;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOuterLevel() {
        return outerLevel;
    }

    public void setOuterLevel(String outerLevel) {
        this.outerLevel = outerLevel;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPolygonPoints() {
        return polygonPoints;
    }

    public void setPolygonPoints(String polygonPoints) {
        this.polygonPoints = polygonPoints;
    }

    public String getRechargeInfo() {
        return rechargeInfo;
    }

    public void setRechargeInfo(String rechargeInfo) {
        this.rechargeInfo = rechargeInfo;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(String serviceCount) {
        this.serviceCount = serviceCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(String startAmount) {
        this.startAmount = startAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreNickName() {
        return storeNickName;
    }

    public void setStoreNickName(String storeNickName) {
        this.storeNickName = storeNickName;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getStoreTel() {
        return storeTel;
    }

    public void setStoreTel(String storeTel) {
        this.storeTel = storeTel;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getWithdrawerBankAccount() {
        return withdrawerBankAccount;
    }

    public void setWithdrawerBankAccount(String withdrawerBankAccount) {
        this.withdrawerBankAccount = withdrawerBankAccount;
    }

    public String getWithdrawerName() {
        return withdrawerName;
    }

    public void setWithdrawerName(String withdrawerName) {
        this.withdrawerName = withdrawerName;
    }
}
