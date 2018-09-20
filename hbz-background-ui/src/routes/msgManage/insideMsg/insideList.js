import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import InsideModal from './insideModal'
import styles from './index.less'

const isSend = {
  0: '否',
  1: '是',
}

const InsideList = ({
  dispatch,
  fields,
  tableTime,
  loading,
  receiveType,
  insideMsgDetail,
  msgType,
  platType,
  sendType,
  visible,
  imgUrlSmall,
  introductionData,
  textShow,
  isPhoneNo,
  isSendTime,
}) => {
  const checkDetail = (data) => {
    dispatch({
      type: 'insideMsg/changeStates',
      payload: {
        insideMsgDetail: data,
        visible: true,
      },
    })
  }
  const addMsg = () => {
    dispatch({
      type: 'insideMsg/changeStates',
      payload: {
        insideMsgDetail: '',
        visible: true,
        isPhoneNo: false,
        isSendTime: false,
      },
    })
  }
  const hideModal = () => {
    dispatch({
      type: 'insideMsg/changeStates',
      payload: {
        visible: false,
        imgUrlSmall: '',
        introductionData: '',
        isPhoneNo: false,
        isSendTime: false,
      },
    })
  }
  const sendMsg = (data) => {
    dispatch({
      type: 'insideMsg/sendMsg',
      payload: {
        id: data,
      },
    })
  }
  const columnsData = [
    {
      title: '操作',
      render: (text, record) => {
        return (
          <div>
            {
              record.ifSend === '0' ? (
                <Popconfirm title="确认要发送吗？" onConfirm={sendMsg.bind(this, record.id)} >
                  <Button type="primary">发送</Button>
                </Popconfirm>
              ) : null
            }
            <Button type="primary" onClick={checkDetail.bind(this, record)}>编辑</Button>
          </div>
        )
      },
    },
    {
      title: '编号',
      dataIndex: 'code',
    },
    {
      title: '消息标题',
      dataIndex: 'title',
    },
    {
      title: '消息小图片',
      dataIndex: 'imagePath',
      render: (text, record) => {
        return (
          <div className={styles.images}>
            <img src={record.imagePath} alt="图片" />
          </div>
        )
      },
    },
    {
      title: '接收平台',
      dataIndex: 'receivePlatformType',
      render: (text, record) => {
        return (
          <span>{record.receivePlatformType.name}</span>
        )
      },
    },
    {
      title: '发送方式',
      dataIndex: 'pushType',
      render: (text, record) => {
        return (
          <span>{record.pushType.name}</span>
        )
      },
    },
    {
      title: '接收人',
      dataIndex: 'consumerType',
      render: (text, record) => {
        return (
          <span>{record.consumerType.name}</span>
        )
      },
    },
    {
      title: '接收用户手机号',
      dataIndex: 'consumerPhoneNo',
    },
    {
      title: '是否已发送',
      dataIndex: 'ifSend',
      render: (text, record) => {
        return (
          <span>{isSend[record.ifSend]}</span>
        )
      },
    },
    {
      title: '摘要',
      dataIndex: 'summary',
      width: 100,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/manager/sitePushMessage/getSitePushMessageListByPage',
    fields: fields || {},
    tableTime,
  }
  const modalProps = {
    dispatch,
    loading,
    receiveType,
    insideMsgDetail,
    msgType,
    platType,
    sendType,
    visible,
    imgUrlSmall,
    introductionData,
    textShow,
    hideModal,
    isPhoneNo,
    isSendTime,
  }
  return (
    <div>
      <Card
        extra={<Button type="primary" onClick={addMsg.bind(this)}>新建</Button>}
      >
        <Ttable {...tableProps} />
        {
          visible && <InsideModal {...modalProps} />
        }
      </Card>
    </div>
  )
}

InsideList.propTypes = {
  dispatch: PropTypes.func,
  fields: PropTypes.object,
  tableTime: PropTypes.number,
  loading: PropTypes.isRequired,
  receiveType: PropTypes.array,
  msgType: PropTypes.array,
  platType: PropTypes.array,
  sendType: PropTypes.array,
  insideMsgDetail: PropTypes.object,
  visible: PropTypes.bool,
  imgUrlSmall: PropTypes.string,
  introductionData: PropTypes.string,
  textShow: PropTypes.bool,
  isPhoneNo: PropTypes.bool,
  isSendTime: PropTypes.bool,
}

export default InsideList
