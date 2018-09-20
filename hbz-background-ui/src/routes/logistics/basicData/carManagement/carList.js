import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import CarModal from './carModal'
import moment from 'moment'

const CarList = ({ dispatch, fields, tableTime, firmModal, carData }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'carManagement/updatefirmModal', payload: { visible: true, firmData: data } })
  }
  // 删除
  const deleteCar = (data) => {
    dispatch({ type: 'carManagement/deleteCars', payload: { id: data.id } })
  }
  const columnsData = [
    {
      title: '车牌号',
      dataIndex: 'numberPlate',
      width: 100,
    },
    {
      title: '车辆类型',
      dataIndex: 'vehicleTypeValue',
      width: 100,
    },
    {
      title: '车主名称',
      dataIndex: 'ownersName',
      width: 120,
    },
    {
      title: '车主电话  ',
      dataIndex: 'ownersTelephone',
      width: 100,
    },
    {
      title: '车主地址',
      dataIndex: 'ownerAddress',
      width: 120,
    },
    {
      title: '车主证件号',
      dataIndex: 'ownerNumber',
      width: 120,
    },
    {
      title: '司机名称',
      dataIndex: 'driverName',
      width: 120,
    },
    {
      title: '司机电话',
      dataIndex: 'driverTelephone',
      width: 120,
    },
    {
      title: '司机地址',
      dataIndex: 'driverAddress',
      width: 120,
    },
    {
      title: '司机证件号',
      dataIndex: 'driverNumber',
      width: 120,
    },
    {
      title: '车长',
      dataIndex: 'carLength',
      width: 80,
    },
    {
      title: '载重',
      dataIndex: 'cargoLoad',
      width: 120,
    },
    {
      title: '保险日期',
      dataIndex: 'insuranceDate',
      width: 120,
      render: (text, record) => {
        text = record.insuranceDate && moment(Number(record.insuranceDate)).format('YYYY-MM-DD HH:mm:ss')
        return (
          <div>{text}</div>
        )
      },
    },
    {
      title: '大检查日期',
      dataIndex: 'bigDate',
      width: 120,
      render: (text, record) => {
        text = record.bigDate && moment(Number(record.bigDate)).format('YYYY-MM-DD HH:mm:ss')
        return (
          <div>{text}</div>
        )
      },
    },
    {
      title: '小检日期',
      dataIndex: 'smallDate',
      width: 120,
      render: (text, record) => {
        text = record.smallDate && moment(Number(record.smallDate)).format('YYYY-MM-DD HH:mm:ss')
        return (
          <div>{text}</div>
        )
      },
    },
    {
      title: '备注',
      dataIndex: 'remarks',
      width: 200,
    },
    {
      title: '操作',
      render: (text, record) => {
        return (
          <div>
            <Button onClick={showModal.bind(this, record)}>修改</Button>
            <Popconfirm title="确定要删除吗？" onConfirm={deleteCar.bind(this, record)}>
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
    sourceUrl: '/manager/vehicleInformationTable',
    fields: fields || {},
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 2300 }}
      />
      {firmModal.visible && <CarModal {...firmModal} carData={carData} dispatch={dispatch} />}
    </Card>
  )
}
CarList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  firmModal: PropTypes.obj,
  carData: PropTypes.array,
}
export default CarList
