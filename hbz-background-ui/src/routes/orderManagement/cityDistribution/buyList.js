import React from 'react'
import PropTypes from 'prop-types'
import { Card } from 'antd'
import { Ttable } from 'components'
import BuyModalForm from './buyModal'
const BuyList = ({
  fields,
  tableTime,
  dispatch,
  modalState,
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
  const showModal = (data) => {
    console.log('buyList')
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
      dispatch({ type: 'cityDistribution/teakUserDataGet', payload: { id: data.id } })
    }
    // 付款详情
    if (data.orderTrans === 'PAID') {
      dispatch({ type: 'cityDistribution/platformDetails', payload: { id: data.id } })
    }
    // 帮我买基础信息
    dispatch({ type: 'cityDistribution/buyDataGet', payload: { id: data.id } })
    data.type = 'buy'
    dispatch({ type: 'cityDistribution/updateModal', payload: { visible: true, firmData: data } })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'cityDistribution/updateModal', payload: { visible: false, firmData: {} } })
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
          <a onClick={showModal.bind(this, record)} >{text}</a>
        )
      },
    },
    {
      title: '订单归属公司',
      dataIndex: 'organizationName',
      width: 130,
    },
    {
      title: '商品名称',
      dataIndex: 'commodityName',
      width: 130,
    },
    {
      title: '购买数量',
      dataIndex: 'commodityCount',
      width: 80,
    },
    {
      title: '预估价格（元）',
      dataIndex: 'commodityAmount',
      width: 100,
    },
    {
      title: '配送费用（元）',
      dataIndex: 'remuneration',
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
      dataIndex: 'creatTime',
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
      dataIndex: 'destArea',
      width: 200,
    },
    {
      title: '接单人',
      dataIndex: 'teakUser',
      width: 130,
    },
    {
      title: '接单人联系电话',
      dataIndex: 'teakUserTelephone',
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
        sourceUrl="/manager/helpMeToBuyPage"
        tableTime={tableTime}
        scroll={{ x: 2300 }}
      />
      <BuyModalForm {...modalState}
        hideModal={hideModal}
        dispatch={dispatch}
        collectionData={collectionData}
        payData={payData}
        teakUserData={teakUserData}
        recruitData={recruitData}
        logisticsModal={logisticsModal}
        buyData={buyData}
        platformData={platformData}
        sendData={sendData}
      />
    </Card>
  )
}

BuyList.propTypes = {
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
}
export default BuyList
