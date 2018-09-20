import React from 'react'
import styles from './Footer.less'
import { Iconfont } from 'components'
import { config } from '../../utils'

const Footer = () => <div className={styles.footer}>
  <span className={styles.copy}>{config.footerText}</span>
  <span className={styles.phone}><Iconfont type="dianhua" className={styles.icon} />{config.footerPhone}</span>
</div>

export default Footer
