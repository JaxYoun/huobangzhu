import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import StartModal from './startwayLoad/startModal'
import StartSureModal from './startwayLoad/startSureModal'
import HalfwayLoadModal from './halfwayLoad/halfwayLoadModal'
import HalfwayunLoadModal from './halfwayunLoad/halfwayunLoadModal'
import StatusModal from './statusModal'
const StartList = ({ dispatch,
  fields,
  tableTime,
  firmModal,
  transportOrder,
  car,
  chooseModal,
  unchooseModal,
  sureModal,
  halfwayLoadModal,
  carHalfway,
  statusModal,
  transitStateData,
  unchooseHalfModal,
  chooseHalfModal,
  transportOrderHalfway,
  halfwayunLoadModal,
  carHalfwayUnLoad,
  unLoadList,
 }) => {
  // 确认发车
  const sureModalShow = (data) => {
    dispatch({ type: 'carStart/getSureData', payload: { id: data.id } })
    dispatch({ type: 'carStart/updateSureModal', payload: { visible: true } })
  }
  // 中途装车
  const showHalfwayLoadModal = (data) => {
    dispatch({ type: 'carStart/halfwayLoadData', payload: { id: data.id } })
    dispatch({ type: 'carStart/updateHalfwayLoadModal', payload: { visible: true } })
  }
  // 中途装车状态编辑
  const updateStatusModal = (data) => {
    dispatch({ type: 'carStart/updateStatusModal', payload: { visible: true, firmData: data } })
  }
  // 新建单编辑
  const updateNewModal = (data) => {
    dispatch({ type: 'carStart/updateNewModal', payload: { id: data.id } })
    dispatch({ type: 'carStart/updatefirmModal', payload: { visible: true } })
  }
  // 中途卸货
  const showHalfwayunLoadModal = (data) => {
    dispatch({ type: 'carStart/halfwayunLoadData', payload: { id: data.id } })
    dispatch({ type: 'carStart/updateHalfwayunLoadModal', payload: { visible: true } })
  }
  const columnsData = [
    {
      title: '发车编号',
      dataIndex: 'startNumber',
      width: 80,
      render: (text, record) => {
        return (<span><a onClick={sureModalShow.bind(this, record)}>{text}</a></span>)
      },
    },
    {
      title: '车牌号',
      dataIndex: 'vehicleInformationDTO.numberPlate',
      width: 100,
    },
    {
      title: '车辆类型',
      dataIndex: 'vehicleInformationDTO.vehicleTypeValue',
      width: 100,
    },
    {
      title: '司机名称',
      dataIndex: 'vehicleInformationDTO.driverName',
      width: 120,
    },
    {
      title: '司机电话',
      dataIndex: 'vehicleInformationDTO.driverTelephone',
      width: 120,
    },
    {
      title: '发站城市',
      dataIndex: 'originArea',
      width: 120,
    },
    {
      title: '到站城市',
      dataIndex: 'destArea',
      width: 120,
    },
    {
      title: '发车时间',
      dataIndex: 'receiptDate',
      width: 120,
    },
    {
      title: '预计到达时间',
      dataIndex: 'receiptToDate',
      width: 120,
    },
    {
      title: '已装重量（kg）',
      dataIndex: 'singleWeight',
      width: 100,
    },
    {
      title: '已装体积（立方米）',
      dataIndex: 'singleVolume',
      width: 120,
    },
    {
      title: '发单状态',
      dataIndex: 'shippingStatusValue',
      width: 100,
    },
    {
      title: '在途状态',
      dataIndex: 'transitStateValue',
      width: 100,
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
            {record.shippingStatusValue === '新建' ? <Button onClick={updateNewModal.bind(this, record)}>修改</Button> : <Button onClick={updateStatusModal.bind(this, record)}>编辑状态</Button>}
            <Button onClick={showHalfwayLoadModal.bind(this, record)}>中途装车</Button>
            <Button onClick={showHalfwayunLoadModal.bind(this, record)}>中途卸货</Button>
          </div>
        )
      },
      width: 200,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/manager/onTheWayPage',
    fields: fields || {},
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 3000 }}
      />
       {firmModal.visible && <StartModal {...firmModal}
         dispatch={dispatch}
         transportOrder={transportOrder}
         car={car}
         chooseModal={chooseModal}
         unchooseModal={unchooseModal}
       />}
      {sureModal.visible && <StartSureModal {...sureModal}
        dispatch={dispatch}
        transportOrder={transportOrder}
      />}
      {halfwayLoadModal.visible && <HalfwayLoadModal {...halfwayLoadModal}
        dispatch={dispatch}
        carHalfway={carHalfway}
        transportOrderHalfway={transportOrderHalfway}
        chooseHalfModal={chooseHalfModal}
        unchooseHalfModal={unchooseHalfModal}
      />}
      {halfwayunLoadModal.visible && <HalfwayunLoadModal {...halfwayunLoadModal}
        dispatch={dispatch}
        carHalfwayUnLoad={carHalfwayUnLoad}
        unLoadList={unLoadList}
      />}
      {statusModal.visible && <StatusModal {...statusModal}
        dispatch={dispatch} transitStateData={transitStateData}
      />}
    </Card>
  )
}
StartList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  firmModal: PropTypes.obj,
  transportOrder: PropTypes.obj,
  car: PropTypes.obj,
  chooseModal: PropTypes.obj,
  unchooseModal: PropTypes.obj,
  sureModal: PropTypes.obj,
  halfwayLoadModal: PropTypes.obj,
  carHalfway: PropTypes.obj,
  statusModal: PropTypes.obj,
  transitStateData: PropTypes.array,
  chooseHalfModal: PropTypes.obj,
  unchooseHalfModal: PropTypes.obj,
  transportOrderHalfway: PropTypes.obj,
  halfwayunLoadModal: PropTypes.obj,
  carHalfwayUnLoad: PropTypes.obj,
  unLoadList: PropTypes.obj,
}
export default StartList
