import React from 'react'
import PropTypes from 'prop-types'
import { Card } from 'antd'
import { Ttable } from 'components'
import ModalLineForm from './modalLine'
const LineList = ({
  fields,
  tableTime,
  dispatch,
  modalState,
  collectionData,
  payData,
  teakUserData,
  recruitData,
  logisticsModal,
}) => {
  // 打开模态框
  const showModal = (data) => {
    // 清空数据
    dispatch({ type: 'specialLine/update', payload: { collectionData: {},
      payData: {},
      teakUserData: {},
      recruitData: [],
    } })
    // 在线支付
    let type = ''
    if (data.settlementType === 'ONLINE_PAYMENT') {
      type = 'onlinePayment'
      if (data.orderTrans !== 'NEW' && data.orderTrans !== 'CONFIRMED') {
        dispatch({ type: 'specialLine/payDataGet', payload: { businessNo: data.orderNo } })
      }
      if (data.orderTrans === 'WAIT_TO_TAKE' || data.orderTrans === 'TRANSPORT'
      || data.orderTrans === 'WAIT_TO_CONFIRM' || data.orderTrans === 'WAIT_FOR_PAYMENT'
      || data.orderTrans === 'PAID' || data.orderTrans === 'LIQUIDATION_COMPLETED') {
        dispatch({ type: 'specialLine/teakUserDataGet', payload: { id: data.id } })
      }
    }
    // 月结
    if (data.settlementType === 'MONTHLY_SETTLEMENT') {
      type = 'monthlySettlemrnt'
      if (data.orderTrans === 'WAIT_TO_TAKE' || data.orderTrans === 'TRANSPORT'
      || data.orderTrans === 'WAIT_TO_CONFIRM' || data.orderTrans === 'WAIT_FOR_PAYMENT'
      || data.orderTrans === 'PAID' || data.orderTrans === 'LIQUIDATION_COMPLETED') {
        dispatch({ type: 'specialLine/teakUserDataGet', payload: { id: data.id } })
      }
    }
    // 车辆征集
    if (data.settlementType === 'LEVY_ONLINE_PAYMENT') {
      // 添加征集条件
      dispatch({ type: 'specialLine/collectionDataGet', payload: { orderId: data.id } })
      // 添加支付详情
      if (data.orderTrans !== 'NEW' && data.orderTrans !== 'CONFIRMED') {
        dispatch({ type: 'specialLine/payDataGet', payload: { businessNo: data.orderNo } })
      }
      // 征集完成
      if (data.orderTrans === 'WAIT_TO_TAKE' || data.orderTrans === 'TRANSPORT'
      || data.orderTrans === 'WAIT_TO_CONFIRM' || data.orderTrans === 'WAIT_FOR_PAYMENT'
      || data.orderTrans === 'PAID' || data.orderTrans === 'LIQUIDATION_COMPLETED') {
        type = 'levyOnlinePaymentWc'
        dispatch({ type: 'specialLine/teakUserDataGet', payload: { id: data.id } })
      }
      // 征集中
      if (data.orderTrans === 'ORDER_TO_BE_RECEIVED' || data.orderTrans === 'LOCKED_ORDER') {
        type = 'levyOnlinePayment'
        dispatch({ type: 'specialLine/recruitDataGet', payload: { id: data.id } })
      }
    }
    data.type = type
    // 获取基础信息
    dispatch({ type: 'specialLine/getData', payload: { id: data.id } })
    dispatch({ type: 'specialLine/updateModal', payload: { visible: true } })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'specialLine/updateModal', payload: { visible: false, firmData: {} } })
    dispatch({ type: 'specialLine/clean' })
  }
    // 编辑订单列表
  const columns = [
    {
      title: '订单编号',
      dataIndex: 'orderNo',
      width: 200,
      render: (text, record) => {
        return (
          <a onClick={showModal.bind(this, record)} >{text}</a>
        )
      },
    },
    {
      title: '订单归属公司',
      dataIndex: 'org',
      width: 120,
    },
    {
      title: '订单创建人',
      dataIndex: 'createUser',
      width: 120,
    },
    {
      title: '创建人电话',
      dataIndex: 'createUsertelephone',
      width: 120,
    },
    {
      title: '订单发布时间',
      dataIndex: 'createUserTime',
      width: 120,
    },
    {
      title: '指定运输到货时间',
      dataIndex: 'destlimit',
      width: 120,
    },
    {
      title: '取货城市',
      dataIndex: 'originArea',
      width: 200,
    },
    {
      title: '取货联系人',
      dataIndex: 'linkMan',
      width: 120,
    },
    {
      title: '取货联系电话',
      dataIndex: 'linkTelephone',
      width: 130,
    },
    {
      title: '送货城市',
      dataIndex: 'destArea',
      width: 200,
    },
    {
      title: '重量(kg)',
      dataIndex: 'commodityWeight',
      width: 80,
    },
    {
      title: '体积(m³)',
      dataIndex: 'commodityVolume',
      width: 80,
    },
    {
      title: '金额(元)',
      dataIndex: 'amount',
      width: 120,
    },
    {
      title: '是否支付',
      dataIndex: 'orderTrans',
      width: 80,
      render: (text, record) => {
        let color = ''
        if (record.orderTrans === 'NEW' || record.orderTrans === 'CONFIRMED') {
          text = '否'
          color = 'red'
        } else {
          text = '是'
          color = 'green'
        }
        return (
          <span style={{ color: `${color}` }}>{text}</span>
        )
      },
    },
    {
      title: '付款方式',
      dataIndex: 'settlementTypeValue',
      width: 100,
    },
    {
      title: '订单类型',
      dataIndex: 'orderTypeValue',
      width: 100,
    },
    {
      title: '订单状态',
      dataIndex: 'orderTransValue',
      width: 100,
    }]

  return (
    <Card>
      <Ttable
        columns={columns}
        fields={fields}
        sourceUrl="/manager/hbzFslOrderpage"
        tableTime={tableTime}
        scroll={{ x: 2500 }}
      />
      <ModalLineForm {...modalState}
        hideModal={hideModal}
        dispatch={dispatch}
        collectionData={collectionData}
        payData={payData}
        teakUserData={teakUserData}
        recruitData={recruitData}
        logisticsModal={logisticsModal}
      />
    </Card>
  )
}

LineList.propTypes = {
  tableTime: PropTypes.string,
  fields: PropTypes.array,
  dispatch: PropTypes.func,
  modalState: PropTypes.object,
  collectionData: PropTypes.obj,
  payData: PropTypes.obj,
  teakUserData: PropTypes.obj,
  recruitData: PropTypes.array,
  logisticsModal: PropTypes.obj,
}
export default LineList
