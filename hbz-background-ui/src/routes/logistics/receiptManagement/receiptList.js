import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import ReceiptModalForm from './receiptModal'

const ReceiptList = ({ dispatch, fields, tableTime, firmModal, goodsModal, goodsMessage, userModal, getuserMessage, senduserMessage }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'receiptManagement/updatefirmModal', payload: { visible: true, firmData: data } })
    // 编辑获取省市区
    if (data.provinceId === 1 || data.provinceId === 18 || data.provinceId === 791 || data.provinceId === 2234) {
      dispatch({ type: 'receiptManagement/updateFromCountys', payload: { parentId: data.provinceId } })
    } else {
      dispatch({ type: 'receiptManagement/updateFromCitys', payload: { parentId: data.provinceId } })
      dispatch({ type: 'receiptManagement/updateFromCountys', payload: { parentId: data.cityId } })
    }
    if (data.provinceToId === '1' || data.provinceToId === '18' || data.provinceToId === '791' || data.provinceToId === '2234') {
      dispatch({ type: 'receiptManagement/updateToCountys', payload: { parentId: data.provinceId } })
    } else {
      dispatch({ type: 'receiptManagement/updateToCitys', payload: { parentId: data.provinceToId } })
      dispatch({ type: 'receiptManagement/updateToCountys', payload: { parentId: data.cityToId } })
    }
  }
  // 删除
  const deleteMessage = (data) => {
    dispatch({ type: 'receiptManagement/deleteMessages', payload: { id: data.id } })
  }
  const columnsData = [
    {
      title: '物流编号',
      dataIndex: 'trackingNumber',
      width: 120,
    },
    {
      title: '运单号',
      dataIndex: 'waybillNumber',
      width: 120,
    },
    {
      title: '收件类型',
      dataIndex: 'goodsType',
      width: 80,
      render: (text, record) => {
        if (record.goodsType === '0') {
          text = '零担'
        } else if (record.goodsType === '1') {
          text = '整车'
        } else {
          text = '快递'
        }
        return (
          <span>{text}</span>
        )
      },
    },
    {
      title: '发站城市',
      dataIndex: 'originArea',
      width: 100,
    },
    {
      title: '到站城市  ',
      dataIndex: 'destArea',
      width: 100,
    },
    {
      title: '货物名称',
      dataIndex: 'commodityName',
      width: 120,
    },
    {
      title: '装车数量',
      dataIndex: 'amount',
      width: 120,
    },
    {
      title: '收货单位',
      dataIndex: 'receiverUserCompanyName',
      width: 120,
    },
    {
      title: '收货电话',
      dataIndex: 'receiverUserTelephone',
      width: 120,
    },
    {
      title: '包装',
      dataIndex: 'packageUnitValue',
      width: 120,
    },
    {
      title: '重量KG',
      dataIndex: 'weight',
      width: 120,
    },
    {
      title: '体积m³',
      dataIndex: 'volume',
      width: 120,
    },
    {
      title: '代收货款',
      dataIndex: 'onCollection',
      width: 120,
    },
    {
      title: '垫付费用',
      dataIndex: 'advancePayment',
      width: 120,
    },
    {
      title: '中转站',
      dataIndex: 'inWar',
      width: 200,
    },
    {
      title: '付款方式',
      dataIndex: 'paymentMethodName',
      width: 120,
    },
    {
      title: '订单状态',
      dataIndex: 'remarks',
      width: 200,
    },
    {
      title: '操作',
      render: (text, record) => {
        return (
          <div>
            <Button onClick={showModal.bind(this, record)}>修改</Button>
            <Popconfirm title="确定要删除吗？" onConfirm={deleteMessage.bind(this, record)}>
              <Button>删除</Button>
            </Popconfirm>
          </div>
        )
      },
      width: 200,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/manager/CargoInformationPage',
    fields: fields || {},
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 3000 }}
      />
      {firmModal.visible && <ReceiptModalForm {...firmModal} goodsModal={goodsModal}
        goodsMessage={goodsMessage} userModal={userModal} dispatch={dispatch} getuserMessage={getuserMessage} senduserMessage={senduserMessage} />}
    </Card>
  )
}
ReceiptList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  firmModal: PropTypes.obj,
  carData: PropTypes.array,
  goodsModal: PropTypes.obj,
  goodsMessage: PropTypes.obj,
  userModal: PropTypes.obj,
  getuserMessage: PropTypes.obj,
  senduserMessage: PropTypes.obj,
}
export default ReceiptList
