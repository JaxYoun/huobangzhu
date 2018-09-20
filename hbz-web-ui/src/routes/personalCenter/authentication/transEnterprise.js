import React from 'react'
import PropTypes from 'prop-types'
import { message } from 'antd'
import { connect } from 'dva'
import { COOKIE } from 'utils'
import Authentication from './authentication'

const TransEnterprise = ({
  dispatch,
  personalCenter,
}) => {
  const { transEnterprise } = personalCenter
  const { iDcardImage, licenceImage, registryData } = transEnterprise
  const uploadProp = {
    title: '运输企业认证',
    headers: {
      'X-AUTH-TOKEN': COOKIE.getCookie('X-AUTH-TOKEN'),
      'X-AUTH-USER': COOKIE.getCookie('X-AUTH-USER'),
    },
    action: '/api/web/common/transImageToBase64',
    beforeUpload (file) {
      console.log(file.type)
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif'
      if (!isJPG) {
        message.error('请上传jpg,png,jpeg或gif格式的图片')
      }
      const isLt2M = file.size / 1024 / 1024 < 3
      if (!isLt2M) {
        message.error('请上传不超过3M的图片')
      }
      return isJPG && isLt2M
    },
    onChange (info) {
      const status = info.file.status
      if (status === 'done') {
        console.log(info.file.response)
        dispatch({
          type: 'personalCenter/updateOther',
          payload: {
            iDcardImage: info.file.response.data.base64Code,
            parent: 'transEnterprise',
          },
        })
      } else if (status === 'error') {
        message.error('文件上传失败')
      }
    },
    onhandleChange (info) {
      const status = info.file.status
      if (status === 'done') {
        dispatch({
          type: 'personalCenter/updateOther',
          payload: {
            licenceImage: info.file.response.data.base64Code,
            parent: 'transEnterprise',
          },
        })
      } else if (status === 'error') {
        message.error('文件上传失败')
      }
    },
    iDcardImage,
    licenceImage,
    registryData,
    dispatch,
    now: 'transEnterprise',
  }
  return (
    <div>
      <Authentication {...uploadProp} />
    </div>
  )
}
TransEnterprise.propTypes = {
  personalCenter: PropTypes.object,
  dispatch: PropTypes.func,
}
export default connect(({ personalCenter }) => ({ personalCenter }))(TransEnterprise)
