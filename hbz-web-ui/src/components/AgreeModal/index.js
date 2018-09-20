import React from 'react'
import PropTypes from 'prop-types'
import { Modal } from 'antd'

class AgreeModal extends React.Component {
  constructor (props) {
    super(props)
    this.state = {}
  }
  render () {
    return (
      <Modal
        visible={this.props.visible}
        title={this.props.title || ''}
        onCancel={this.props.hideModal}
        onOk={this.props.hideModal}
      >
        <p>{this.props.content}</p>
      </Modal>
    )
  }
}

AgreeModal.propTypes = {
  visible: PropTypes.bool,
  title: PropTypes.string,
  hideModal: PropTypes.func,
  content: PropTypes.func,
}
export default AgreeModal
