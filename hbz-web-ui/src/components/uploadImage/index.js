import React from 'react'
import PropTypes from 'prop-types'
import { Upload, Icon, message } from 'antd'

function getBase64 (img, callback) {
  const reader = new FileReader()
  reader.addEventListener('load', () => callback(reader.result))
  reader.readAsDataURL(img)
}

function beforeUpload (file) {
  const isJPG = file.type === 'image/jpeg'
  if (!isJPG) {
    message.error('只能上传jpg格式图片!')
  }
  const isLt200kb = file.size / 1024 < (5 * 1024)
  if (!isLt200kb) {
    message.error('图片需要小于5mb!')
  }
  return isJPG && isLt200kb
}

class UploadImage extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
    }
  }
  handleChange = (info) => {
    const names = this.props.name
    const nameForm = this.props.nameForm
    getBase64(info.file.originFileObj, imageUrl => {
      this.props.dispatch({
        type: this.props.type,
        payload: { [names]: imageUrl },
      })
    })
    if (info.file.status === 'done') {
      this.props.form.setFieldsValue({
        [nameForm]: `${info.file.response.data.fileList[0].filePath}`,
      })
    } else if (info.file.status === 'error') {
      message.error(`${info.file.name} 图片选择失败请重新选择.`)
    }
  }

  render () {
    const imageUrl = this.props.imgUrl || ''
    const data = {
      files: 'MultipartFile',
      fileTypeId: '1',
    }
    return (
      <Upload
        className="avatar-uploader"
        name="file"
        data={data}
        showUploadList={false}
        beforeUpload={beforeUpload}
        onChange={this.handleChange}
        action="/api/upload/uploadFiles"
      >
        {
          imageUrl ?
            <img src={imageUrl} alt="点击上传图片" className="avatar" /> :
            <Icon type="plus" className="avatar-uploader-trigger" />
        }
      </Upload>
    )
  }
}
UploadImage.propTypes = {
  uploadImgs: PropTypes.func,
  imgUrl: PropTypes.string,
  dispatch: PropTypes.func,
  type: PropTypes.string,
  name: PropTypes.string,
  nameForm: PropTypes.string,
  form: PropTypes.object,
}
export default UploadImage
