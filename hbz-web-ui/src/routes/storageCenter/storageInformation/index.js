import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button, message } from 'antd'
import { Ttable } from 'components'
import { browserHistory } from 'react-router'
import styles from './index.less'

const StorageInformation = ({
  dispatch,
  storageInformation,
}) => {
  const { fields, tableTime } = storageInformation
  const checkStorageDetail = (id) => {
    browserHistory.push(`/storageCenter/storageInformation/storageDetail?orderId=${id}`)
  }
  // const storageData = storageList.map(item => {
  //   return (
  //     <Card className={styles.card} onClick={checkStorageDetail.bind(this, item.id)}>
  //       <Row>
  //         <Col span={4}>
  //           <div className={styles['logo-img']}>
  //             <img alt="照片" src={item.titleImageList[0]} />
  //           </div>
  //         </Col>
  //         <Col span={19} offset={1}>
  //           <div className={styles.title}>
  //             {`${item.address},${item.name},${item.capacity}平`}
  //           </div>
  //           <div className={styles.price}>
  //             {`￥${item.unitPrice}元`}
  //           </div>
  //           <div>
  //             {item.warehouseDescribe}
  //           </div>
  //         </Col>
  //       </Row>
  //     </Card>
  //   )
  // })
  const columnsData = [
    {
      render: (text, record) => {
        return (
          <Card className={styles.card} onClick={checkStorageDetail.bind(this, record.id)}>
            <Row>
              <Col span={4}>
                <div className={styles['logo-img']}>
                  <img alt="照片" src={record.titleImageList[0]} />
                </div>
              </Col>
              <Col span={19} offset={1}>
                <div className={styles.title}>
                  {record.name}
                </div>
                <div className={styles.price}>
                  {`￥${record.unitPrice}元`}
                </div>
                <div>
                  {record.warehouseDescribe}
                </div>
              </Col>
            </Row>
          </Card>
        )
      },
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/web/warehouse/getAvailableWarehouseListByPage',
    fields: fields || {},
    tableTime,
    showHeader: false,
    bordered: false,
  }
  return (
    <Card>
      {/* {
        storageData
      } */}
      <Ttable {...tableProps} />
    </Card>
  )
}

StorageInformation.propTypes = {
  storageInformation: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
}

export default connect(({ storageInformation }) => ({ storageInformation }))(StorageInformation)
