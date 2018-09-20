import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Button, Icon, Popconfirm, message } from 'antd'
import { Iconfont } from 'components'
import MsgModal from './msgModal'
import Styles from './index.less'

const Accessories = ({
  accessories,
  dispatch,
  app,
}) => {
  const addressType = {
    1: '未知',
    2: '家',
    3: '公司',
    4: '常用地',
  }
  const { isShow,
    isShowMore,
    visible,
    sendAddress,
    receiptAddress,
    defaultsendAddress,
    defaultreceiptAddress,
    changeSendAddress,
    changeReceiptAddress,
    modalType,
    formData,
    addlng,
    addlat,
    commodityType,
    price,
    commodityDetial,
    readyToPay,
  } = accessories
  let { tableData: { massList } } = app
  // 点击展开更多的地址
  const showMore = (type, msg) => {
    dispatch({
      type: 'accessories/showMoreAddress',
      payload: { isShow: msg, types: type },
    })
  }
  // 点击打开模态框
  const showModal = (type, data, event) => {
    if (data) {
      event.stopPropagation()
    }
    dispatch({
      type: 'accessories/showModal',
      payload: {
        modalType: type,
        formData: data || '',
      },
    })
  }
  // 设置默认地址
  const setDefaultAddress = (data, event) => {
    event.stopPropagation()
    let { id, address, type, linker, linkTelephone, lng, lat, location, usein } = data
    let obj = {
      // areaCode: data.area.outCode,
      id,
      address,
      type,
      linker,
      linkTelephone,
      lng,
      lat,
      usein,
      location,
      idefault: 1,
    }
    dispatch({ type: 'accessories/updateAddress', payload: obj })
  }
  const stop = (event) => {
    event.stopPropagation()
  }
  // 删除地址
  const confimDelete = (data, text, event) => {
    console.log(event)
    event.stopPropagation()
    console.log('confimDelete', text)
    dispatch({ type: 'accessories/deleteAddress', payload: { data, now: text } })
  }
  // 显示货物详情
  const displayCommodityDetial = (data) => {
    const array = []
    array.push(commodityType[data.commodityClass])
    for (let item of massList) {
      if (data.commodityWeight === item.val) {
        array.push(item.name)
      }
    }
    array.push(data.commodityVolume)
    dispatch({ type: 'accessories/changeStates', payload: { commodityDetial: array, readyToPay: data } })
  }
  // 计算订单价格
  const countOrderPrice = (data) => {
    const sendData = changeSendAddress || defaultsendAddress
    const receiptData = changeReceiptAddress || defaultreceiptAddress
    displayCommodityDetial(data)
    if (sendData && receiptData) {
      let { commodityClass, commodityWeight, commodityVolume } = data
      const originAreaCode = sendData.area.outCode
      const destAreaCode = receiptData.area.outCode
      let obj = {
        originAreaCode,
        destAreaCode,
        commodityClass,
        commodityWeight,
        commodityVolume,
      }
      dispatch({ type: 'accessories/countPrice', payload: obj })
    } else {
      message.error('请选择收货或发货地址！')
    }
  }
  const modalProps = {
    visible,
    dispatch,
    modalType,
    formData,
    addlng,
    addlat,
    commodityType,
    massList,
    countOrderPrice,
    readyToPay,
  }
  // 点击选择发货地址
  const selectSendAddress = (data) => {
    dispatch({
      type: 'accessories/changeStates',
      payload: {
        changeSendAddress: data,
      },
    })
  }
  // 点击选择收货地址
  const selectReceiptAddress = (data) => {
    dispatch({
      type: 'accessories/changeStates',
      payload: {
        changeReceiptAddress: data,
      },
    })
  }
  // 去支付
  const goPay = () => {
    const sendData = changeSendAddress || defaultsendAddress
    const receiptData = changeReceiptAddress || defaultreceiptAddress
    if (readyToPay && sendData && receiptData) {
      let {
        lng: originX,
        lat: originY,
        address: originAddr,
        linker: originLinker,
        linkTelephone: originTelephone,
      } = sendData
      let {
        lng: destX,
        lat: destY,
        address: destAddr,
        linker: linker,
        linkTelephone: telephone,
      } = receiptData
      let obj = {
        originX,
        originY,
        destX,
        destY,
        amount: price,
        originAreaCode: sendData.area.outCode,
        destAreaCode: receiptData.area.outCode,
        originAddr,
        destAddr,
        originLinker,
        originTelephone,
        linker,
        telephone,
        ...readyToPay,
      }
      dispatch({ type: 'accessories/exCreate', payload: obj })
    } else {
      if (!readyToPay) {
        message.error('请填写货物详情！')
      } else if (!sendData) {
        message.error('请选择发货地址')
      } else {
        message.error('请选择收货地址')
      }
    }
  }
  // 生成常用发货地址
  const sendAddressData = sendAddress.map(item => {
    return (
      <li className={Styles['send-address-list']} onClick={selectSendAddress.bind(this, item)}>
        <div className={Styles.user}>
          <h4>
            {item.linker}<i>{addressType[item.type]}</i> <span>{item.linkTelephone}</span>
          </h4>
          <div className={Styles.options}>
            <span>{item.location}</span>
            <span>
              <Button onClick={setDefaultAddress.bind(this, item)}>设为默认地址</Button>
              <Button className={Styles.edit} onClick={showModal.bind(this, 'editFahuo', item)}>编辑</Button>
              <Popconfirm title="确认删除吗?" onConfirm={confimDelete.bind(this, item, 'sendAddress')}>
                <Button className={Styles.delete} onClick={stop.bind(this)}>删除</Button>
              </Popconfirm>
            </span>
          </div>
        </div>
      </li>
    )
  })
  // 生成常用收货地址
  const receiptAddressData = receiptAddress.map(item => {
    return (
      <li onClick={selectReceiptAddress.bind(this, item)}>
        <div className={Styles.user}>
          <h4>
            {item.linker}<i>{addressType[item.type]}</i> <span>{item.linkTelephone}</span>
          </h4>
          <div className={Styles.options}>
            <span>{item.location}</span>
            <span>
              <Button onClick={setDefaultAddress.bind(this, item)}>设为默认地址</Button>
              <Button className={Styles.edit} onClick={showModal.bind(this, 'editShouhuo', item)}>编辑</Button>
              <Popconfirm title="确认删除吗?" onConfirm={confimDelete.bind(this, item, 'receiptAddress')}>
                <Button className={Styles.delete} onClick={stop.bind(this)}>删除</Button>
              </Popconfirm>
            </span>
          </div>
        </div>
      </li>
    )
  })
  return (
    <div>
      <Card>
        <div className={Styles['address-list']}>
          <Button icon="plus" onClick={showModal.bind(this, 'fahuo', '')}>添加发货地址</Button>
          {
            !defaultsendAddress && !changeSendAddress ? (
              <div className={Styles.user}>
                <div className={Styles.options}>
                  <span>请选择发货地址</span>
                  <span onClick={showMore.bind(this, 'fahuo', !isShow)}>
                    <i className={isShow ? Styles.turnright : Styles.turnleft}><b></b></i>
                    <a>{isShow ? '收起' : '更多发货地址'}</a>
                  </span>
                </div>
              </div>
            ) : (
              <div className={Styles.user}>
                <Iconfont type="location" className={Styles.location} />
                <h4>
                  {changeSendAddress ? changeSendAddress.linker : defaultsendAddress.linker}
                  <i>{changeSendAddress ? addressType[changeSendAddress.type] : addressType[defaultsendAddress.type]}</i>
                  <span className={Styles.moreAddress}>
                    {changeSendAddress ? changeSendAddress.linkTelephone : defaultsendAddress.linkTelephone}
                  </span>
                </h4>
                <div className={Styles.options}>
                  <span>
                    {changeSendAddress ? changeSendAddress.location : defaultsendAddress.location}
                  </span>
                  <span onClick={showMore.bind(this, 'fahuo', !isShow)}>
                    <i className={isShow ? Styles.turnright : Styles.turnleft}><b></b></i>
                    <a>更多发货地址</a>
                  </span>
                </div>
              </div>
            )
          }
        {isShow &&
          <ul>
            {sendAddressData}
          </ul>
          }
        </div>
        <div className={Styles['address-list']}>
          <Button icon="plus" onClick={showModal.bind(this, 'shouhuo', '')}>添加收货地址</Button>
          {
            !changeReceiptAddress && !defaultreceiptAddress ? (
              <div className={Styles.user}>
                <div className={Styles.options}>
                  <span>请选择收货地址</span>
                  <span onClick={showMore.bind(this, 'shouhuo', !isShowMore)}>
                    <i className={isShowMore ? Styles.turnright : Styles.turnleft}><b></b></i>
                    <a>{isShowMore ? '收起' : '更多收货地址'}</a>
                  </span>
                </div>
              </div>
            ) : (
              <div className={Styles.user}>
                <Iconfont type="location" className={Styles.locationblue} />
                <h4>
                  {changeReceiptAddress ? changeReceiptAddress.linker : defaultreceiptAddress.linker}
                  <i>{changeReceiptAddress ? addressType[changeReceiptAddress.type] : addressType[defaultreceiptAddress.type]}</i>
                  <span className={Styles.moreAddress}>
                    {changeReceiptAddress ? changeReceiptAddress.linkTelephone : defaultreceiptAddress.linkTelephone}
                  </span>
                </h4>
                <div className={Styles.options}>
                  <span>
                    {changeReceiptAddress ? changeReceiptAddress.location : defaultreceiptAddress.location}
                  </span>
                  <span onClick={showMore.bind(this, 'shouhuo', !isShowMore)}>
                    <i className={isShowMore ? Styles.turnright : Styles.turnleft}><b></b></i>
                    <a>更多发货地址</a>
                  </span>
                </div>
              </div>
            )
          }
        {isShowMore &&
          <ul>
            {receiptAddressData}
          </ul>
        }
        </div>
      </Card>
      <Card>
        <div className={Styles.detail}>
          <h4>货物详情</h4>
          <div>
            <Button onClick={showModal.bind(this, 'huowu', '')} ><Icon type="plus" /></Button>
          </div>
          <div className={Styles.details}>
            {
              commodityDetial.map((item, index) => (
                <span>{`${item}${index === 2 ? 'm³' : ''}`}</span>
              ))
            }
          </div>
        </div>
        <div className={Styles.detail}>
          <h4>费用详情</h4>
          <div>
            <p>预估费用：</p>
            <div className={Styles.cost}>
              <span>￥</span>
              <span>{price}</span>
            </div>
          </div>
        </div>
      </Card>
      <Card>
        <div className={Styles.gocost}>
          <Button onClick={goPay}>去支付</Button>
        </div>
      </Card>
      {visible && <MsgModal {...modalProps} />}
    </div>
  )
}

Accessories.propTypes = {
  form: PropTypes.isRequired,
  accessories: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  app: PropTypes.isRequired,
}


export default connect(({ accessories, app }) => ({ accessories, app }))(Accessories)
