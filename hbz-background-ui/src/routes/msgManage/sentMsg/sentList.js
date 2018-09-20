import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import SentModal from './sentModal'
import styles from './index.less'
import moment from 'moment'

const SentList = ({
  dispatch,
  fields,
  tableTime,
  visible,
  sendMsgDetali,
}) => {
  const checkDetail = (data) => {
    dispatch({
      type: 'sentMsg/changeStates',
      payload: {
        visible: true,
        sendMsgDetali: data,
      },
    })
  }
  const hideModal = () => {
    dispatch({
      type: 'sentMsg/changeStates',
      payload: {
        visible: false,
      },
    })
  }
  const columnsData = [
    {
      title: '操作',
      render: (text, record) => {
        return (
          <Button type="primary" onClick={checkDetail.bind(this, record)}>详情</Button>
        )
      },
    },
    {
      title: '编号',
      dataIndex: 'code',
      render: (text, record) => {
        return (
          <span>{record.sitePushMessage.code}</span>
        )
      },
    },
    {
      title: '消息标题',
      dataIndex: 'title',
      render: (text, record) => {
        return (
          <span>{record.sitePushMessage.title}</span>
        )
      },
    },
    {
      title: '消息小图片',
      dataIndex: 'imagePath',
      render: (text, record) => {
        return (
          <div className={styles.images}>
            <img src={record.sitePushMessage.imagePath} alt="图片" />
          </div>
        )
      },
    },
    {
      title: '消息类型',
      dataIndex: 'messageType',
      render: (text, record) => {
        return (
          <span>{record.sitePushMessage.messageType.name}</span>
        )
      },
    },
    {
      title: '接收人',
      dataIndex: 'consumerType',
      render: (text, record) => {
        return (
          <span>{record.sitePushMessage.consumerType.name}</span>
        )
      },
    },
    {
      title: '接收用户手机号',
      dataIndex: 'consumerPhoneNo',
      render: (text, record) => {
        return (
          <span>{record.sitePushMessage.consumerPhoneNo}</span>
        )
      },
    },
    {
      title: '是否已阅读',
      dataIndex: 'ifRead',
      // render: (text, record) => {
      //   return (
      //     <span>{isReader[record.sitePushMessage.isRead]}</span>
      //   )
      // },
    },
    {
      title: '阅读时间',
      dataIndex: 'lastUpdateDate',
      render: (text, record) => {
        return (
          <span>{moment(record.lastUpdateDate).format('YYYY-MM-DD HH:mm:ss')}</span>
        )
      },
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/manager/sitePushMessage/getSitePushMessageRecordListByPage',
    fields: fields || {},
    tableTime,
  }
  const modalProps = {
    visible,
    hideModal,
    sendMsgDetali,
  }
  return (
    <div>
      <Card>
        <Ttable {...tableProps} />
        {
          visible && <SentModal {...modalProps} />
        }
      </Card>
    </div>
  )
}

SentList.propTypes = {
  dispatch: PropTypes.func,
  fields: PropTypes.object,
  tableTime: PropTypes.number,
  loading: PropTypes.isRequired,
  receiveType: PropTypes.array,
  msgType: PropTypes.array,
  platType: PropTypes.array,
  sendMsgDetali: PropTypes.object,
  visible: PropTypes.bool,
  imgUrlSmall: PropTypes.string,
  introductionData: PropTypes.string,
  textShow: PropTypes.bool,
}

export default SentList
