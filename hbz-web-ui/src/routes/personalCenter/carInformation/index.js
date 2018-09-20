import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Row, Button } from 'antd'
import { Iconfont, Ttable } from 'components'
import CarModal from './carModal'
import styles from './index.less'

const CarInformation = ({
  dispatch,
  carInformation,
  loading,
}) => {
  const { fields, tableTime, userInfo, register, visible, transTypes, carDetail, carSizeModal, selectCar } = carInformation
  const showCarModal = (data) => {
    dispatch({
      type: 'carInformation/changeStates',
      payload: {
        visible: true,
        carDetail: data || '',
      },
    })
  }
  const columnsData = [
    {
      render: (text, record) => {
        return (
          <Card
            className={styles.card}
            title={
              <div className={styles.title}>
                <Iconfont type="huoche21" className={styles.send} />
                {/* <span>{record.plateNumber}</span> */}
              </div>
            }
          >
            <Row className={styles.content}>
              <div>
                {record.plateNumber}
              </div>
              <div>
                {record.transTypeName}
              </div>
              <div>
                <Button type="primary" onClick={showCarModal.bind(this, record)}>修改车辆信息</Button>
              </div>
            </Row>
          </Card>
        )
      },
    },
  ]
  const hideModal = () => {
    dispatch({
      type: 'carInformation/changeStates',
      payload: {
        visible: false,
        selectCar: [],
      },
    })
  }
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/vehicleInformation/queryPage',
    fields: fields || {},
    tableTime,
    showHeader: false,
    bordered: false,
  }
  const modalProps = {
    visible,
    hideModal,
    transTypes,
    loading,
    carDetail,
    dispatch,
    carSizeModal,
    selectCar,
  }
  return (
    <div>
      <Card title="车辆信息">
        <Row className={styles.carInfo}>
          <div className={styles.img}>
            <img alt="照片" src={userInfo && userInfo.imageUrl} />
          </div>
          <div className={styles.content}>
            <p>{userInfo.telephone}</p>
            <p>运输企业</p>
            <Button type="primary" onClick={showCarModal.bind(this, '')}>添加车辆</Button>
          </div>
        </Row>
      </Card>
      <Card title="已添加的车辆">
        <Ttable {...tableProps} />
      </Card>
      {
        visible && <CarModal {...modalProps} />
      }
    </div>
  )
}

CarInformation.propTypes = {
  dispatch: PropTypes.func.isRequired,
  carInformation: PropTypes.object,
  loading: PropTypes.object,
}

export default connect(({ carInformation, loading }) => ({ carInformation, loading }))(CarInformation)
