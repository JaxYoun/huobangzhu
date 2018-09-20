import React from 'react'
import PropTypes from 'prop-types'
import { Card } from 'antd'
import { Ttable } from 'components'
import ExpressModalForm from './expressModal'

const ExpressList = ({
  fields,
  tableTime,
  dispatch,
  commonData,
  payData,
  modalState,
  logisticsModal,
  selectData,
  addExpressModal,
}) => {
  // 打开模态框
  const showModal = (data) => {
    // 支付详情
    if (data.orderTrans === 'ORDER_TO_BE_RECEIVED' || data.orderTrans === 'WAIT_TO_TAKE') {
      dispatch({ type: 'expressOrder/payDataGet', payload: { businessNo: data.orderNo } })
    }

    // 获取基础信息
    dispatch({ type: 'expressOrder/getData', payload: { id: data.id } })
    dispatch({ type: 'expressOrder/updateModal', payload: { visible: true, firmData: data } })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'expressOrder/updateModal', payload: { visible: false, firmData: {} } })
    dispatch({ type: 'expressOrder/clean' })
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
      width: 120,
    },
    {
      title: '订单创建人',
      dataIndex: 'createUser',
      width: 120,
    },
    {
      title: '创建人电话',
      dataIndex: 'createUserTelephone',
      width: 120,
    },
    {
      title: '订单发布时间',
      dataIndex: 'createTime',
      width: 120,
    },
    {
      title: '取件城市',
      dataIndex: 'originArea',
      width: 200,
    },
    {
      title: '取件联系人',
      dataIndex: 'originLinker',
      width: 120,
    },
    {
      title: '取件联系电话',
      dataIndex: 'originTelephone',
      width: 130,
    },
    {
      title: '收件城市',
      dataIndex: 'destArea',
      width: 200,
    },
    {
      title: '收件联系人',
      dataIndex: 'linker',
      width: 120,
    },
    {
      title: '收件联系电话',
      dataIndex: 'telephone',
      width: 130,
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
      title: '订单状态',
      dataIndex: 'orderTransValue',
      width: 100,
    },
    {
      title: '第三方快递号',
      dataIndex: 'trackingNumber',
      width: 100,
    },
    {
      title: '快递转运公司',
      dataIndex: 'expressCompanyTypeValue',
      width: 100,
    },
  ]

  return (
    <Card>
      <Ttable
        columns={columns}
        fields={fields}
        sourceUrl="/manager/hbzExpressPiecesPage"
        tableTime={tableTime}
        scroll={{ x: 2500 }}
      />
      <ExpressModalForm
        hideModal={hideModal}
        dispatch={dispatch}
        commonData={commonData}
        payData={payData}
        {...modalState}
        logisticsModal={logisticsModal}
        selectData={selectData}
        addExpressModal={addExpressModal}
      />
    </Card>
  )
}

ExpressList.propTypes = {
  tableTime: PropTypes.string,
  fields: PropTypes.array,
  dispatch: PropTypes.func,
  modalState: PropTypes.object,
  commonData: PropTypes.obj,
  payData: PropTypes.object,
  outSourceData: PropTypes.obj,
  logisticsModal: PropTypes.obj,
  selectData: PropTypes.obj,
  addExpressModal: PropTypes.obj,
}
export default ExpressList
