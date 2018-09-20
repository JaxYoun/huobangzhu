import React from 'react'
import PropTypes from 'prop-types'
import { Card } from 'antd'
import { Ttable } from 'components'
import SendModalForm from './sendModal'
const SendList = ({
  fields,
  tableTime,
  dispatch,
  modalStateSend,
  collectionData,
  payData,
  teakUserData,
  recruitData,
  logisticsModal,
  buyData,
  sendData,
  platformData,
}) => {
  // 打开模态框
  const showModalsend = (data) => {
    console.log('sendList')
    // 清空数据
    dispatch({ type: 'cityDistribution/clean' })
    // 支付详情
    if (data.orderTrans !== 'NEW' && data.orderTrans !== 'CONFIRMED') {
      dispatch({ type: 'cityDistribution/payDataGet', payload: { businessNo: data.orderNo } })
    }
    // 接单人详情
    if (data.orderTrans === 'WAIT_TO_TAKE' || data.orderTrans === 'TRANSPORT'
    || data.orderTrans === 'WAIT_TO_CONFIRM' || data.orderTrans === 'WAIT_FOR_PAYMENT'
    || data.orderTrans === 'PAID' || data.orderTrans === 'LIQUIDATION_COMPLETED') {
      dispatch({ type: 'cityDistribution/teakUserSendDataGet', payload: { id: data.id } })
    }
    // 付款详情
    if (data.orderTrans === 'PAID') {
      dispatch({ type: 'cityDistribution/platformDetails', payload: { id: data.id } })
    }
    // 帮我送基础信息
    dispatch({ type: 'cityDistribution/sendDataGet', payload: { id: data.id } })
    data.type = 'send'
    dispatch({ type: 'cityDistribution/updateModalSend', payload: { visible: true, firmData: data } })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'cityDistribution/updateModalSend', payload: { visible: false, firmData: {} } })
    dispatch({ type: 'cityDistribution/clean' })
  }
    // 编辑订单列表
  const columns = [
    {
      title: '订单编号',
      dataIndex: 'orderNo',
      width: 200,
      render: (text, record) => {
        return (
          <a onClick={showModalsend.bind(this, record)} >{text}</a>
        )
      },
    },
    {
      title: '订单归属公司',
      dataIndex: 'organizationName',
      width: 130,
    },
    {
      title: '取货地址',
      dataIndex: 'originAddress',
      width: 130,
    },
    {
      title: '取货联系人',
      dataIndex: 'originLinker',
      width: 100,
    },
    {
      title: '取货联系电话',
      dataIndex: 'originLinkTelephone',
      width: 130,
    },
    {
      title: '配送费用（元）',
      dataIndex: 'amount',
      width: 100,
    },
    {
      title: '订单创建人',
      dataIndex: 'createUser',
      width: 100,
    },
    {
      title: '创建人电话',
      dataIndex: 'createUserTelephone',
      width: 130,
    },
    {
      title: '订单发布时间',
      dataIndex: 'createTime',
      width: 130,
    },
    {
      title: '配送时间要求',
      dataIndex: 'timeLimitValue',
      width: 100,
    }, {
      title: '指定配送到货时间',
      dataIndex: 'startTime',
      width: 130,
    },
    {
      title: '订单归属城市',
      dataIndex: 'originArea',
      width: 130,
    },
    {
      title: '接单人',
      dataIndex: 'takeUser',
      width: 100,
    },
    {
      title: '接单人联系电话',
      dataIndex: 'takeUserTelephone',
      width: 130,
    },
    {
      title: '是否支付',
      dataIndex: 'orderTrans',
      width: 60,
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
    }, {
      title: '订单状态',
      dataIndex: 'orderTransValue',
      width: 100,
    }]

  return (
    <Card>
      <Ttable
        columns={columns}
        fields={fields}
        sourceUrl="/manager/HelpSendPage"
        tableTime={tableTime}
        scroll={{ x: 2300 }}
      />
      <SendModalForm {...modalStateSend}
        hideModal={hideModal}
        dispatch={dispatch}
        collectionData={collectionData}
        payData={payData}
        teakUserData={teakUserData}
        recruitData={recruitData}
        logisticsModal={logisticsModal}
        buyData={buyData}
        sendData={sendData}
        platformData={platformData}
      />
    </Card>
  )
}

SendList.propTypes = {
  tableTime: PropTypes.string,
  fields: PropTypes.array,
  dispatch: PropTypes.func,
  modalState: PropTypes.object,
  collectionData: PropTypes.obj,
  payData: PropTypes.obj,
  teakUserData: PropTypes.obj,
  recruitData: PropTypes.array,
  logisticsModal: PropTypes.obj,
  buyData: PropTypes.obj,
  sendData: PropTypes.obj,
  platformData: PropTypes.obj,
  modalStateSend: PropTypes.obj,
}
export default SendList
