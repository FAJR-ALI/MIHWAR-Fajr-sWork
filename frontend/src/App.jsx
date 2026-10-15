import { useEffect, useState } from 'react'
import './App.css'
import api from './api'
import { getMapNodes, getRoute } from './services/navigationService'

function Logo({ large = false }) {
  return (
      <div className={`m-logo ${large ? 'm-logo-large' : ''}`}>
        <svg viewBox="0 0 100 100" aria-label="MIHWAR logo">
          {Array.from({ length: 16 }, (_, i) => (
              <g key={i} transform={`rotate(${i * 22.5} 50 50)`}>
                <path
                    d="M50 10 L55 24 L50 34 L45 24 Z"
                    fill={i % 2 === 0 ? '#6547FF' : '#17172D'}
                />
              </g>
          ))}
          <circle cx="50" cy="50" r="11" fill="#6547FF" />
          <circle cx="50" cy="50" r="4" fill="#FFFFFF" />
        </svg>
      </div>
  )
}

function App() {
  const [activeSection, setActiveSection] = useState('dashboard')
  const [destinations, setDestinations] = useState([])
  const [mapNodes, setMapNodes] = useState([])
  const [destinationsLoading, setDestinationsLoading] = useState(true)
  const [mapLoading, setMapLoading] = useState(true)
  const [selectedDestinationId, setSelectedDestinationId] = useState('')
  const [route, setRoute] = useState(null)
  const [routeLoading, setRouteLoading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    const loadDestinations = async () => {
      try {
        const response = await api.get('/destinations')
        const data = Array.isArray(response.data) ? response.data : []

        setDestinations(data)

        const saudi = data.find((item) =>
            item.name?.toLowerCase().includes('saudi')
        )

        setSelectedDestinationId(
            String(saudi?.id ?? data[0]?.id ?? '')
        )
      } catch (err) {
        console.error(err)
        setError('Could not load destinations. Check the backend.')
      } finally {
        setDestinationsLoading(false)
      }
    }

    const loadMapNodes = async () => {
      try {
        const data = await getMapNodes()
        setMapNodes(Array.isArray(data) ? data : [])
      } catch (err) {
        console.error(err)
        setError('Could not load map locations. Check the backend.')
      } finally {
        setMapLoading(false)
      }
    }

    loadDestinations()
    loadMapNodes()
  }, [])

  const navigateTo = (section) => {
    setActiveSection(section)
    setError('')
  }

  const clearRoute = () => {
    setRoute(null)
    setError('')
  }

  const selectedDestination = destinations.find(
      (item) => String(item.id) === selectedDestinationId
  )

  const handleFindRoute = async () => {
    setRoute(null)
    setError('')

    if (!selectedDestinationId) {
      setError('Please select a destination.')
      return
    }

    const startNode = mapNodes.find(
        (node) => node.name?.trim().toLowerCase() === 'main entrance'
    )

    if (!startNode) {
      setError('Main Entrance was not found in the map nodes.')
      return
    }

    const nodes = mapNodes.filter((node) => {
      const id = node.destination?.id ?? node.destinationId
      return String(id ?? '') === selectedDestinationId
    })

    const endNode =
        nodes.find((node) => node.nodeType?.toUpperCase() === 'ENTRY') ??
        nodes.find((node) => node.nodeType?.toUpperCase() === 'ZONE') ??
        nodes.find((node) => node.nodeType?.toUpperCase() === 'SERVICE')

    if (!endNode) {
      setError('No map node is linked to this destination.')
      return
    }

    try {
      setRouteLoading(true)

      const data = await getRoute(startNode.id, endNode.id)

      if (!Array.isArray(data?.points) || data.points.length === 0) {
        throw new Error('No route was returned by the backend.')
      }

      if (
          !data.points.every(
              (point) =>
                  Number.isFinite(Number(point.x)) &&
                  Number.isFinite(Number(point.y))
          )
      ) {
        throw new Error('The route coordinates are invalid.')
      }

      setRoute(data)
    } catch (err) {
      console.error(err)
      setError(
          err.response?.data?.message ||
          err.message ||
          'Could not calculate the route.'
      )
    } finally {
      setRouteLoading(false)
    }
  }

  const pageInfo = {
    dashboard: ['Dashboard', 'Your Expo experience starts here.'],
    identity: ['Digital Identity', 'Your visitor identity in one place.'],
    map: ['Smart Interactive Map', 'Explore Expo Riyadh with ease.'],
    passport: ['Digital Passport', 'Your Expo visitor passport services.'],
    wallet: ['Digital Wallet', 'Your wallet and payment services.'],
  }

  const renderMap = () => (
      <section className="section">
        <div className="section-heading">
          <div>
            <span className="eyebrow">EXPLORE EXPO RIYADH</span>
            <h2>Smart Interactive Map</h2>
            <p>Choose a destination and find your route.</p>
          </div>
          <span className="map-tag">EXPO MAP</span>
        </div>

        <div className="map-card">
          <div className="map-controls">
            <label htmlFor="destination-select">Your destination</label>

            <select
                id="destination-select"
                value={selectedDestinationId}
                onChange={(event) => {
                  setSelectedDestinationId(event.target.value)
                  clearRoute()
                }}
                disabled={destinationsLoading || destinations.length === 0}
            >
              <option value="">Select a destination</option>
              {destinations.map((destination) => (
                  <option key={destination.id} value={String(destination.id)}>
                    {destination.name}
                  </option>
              ))}
            </select>

            <div className="map-buttons">
              <button
                  className="map-action-btn"
                  onClick={handleFindRoute}
                  disabled={
                      destinationsLoading ||
                      mapLoading ||
                      routeLoading ||
                      !selectedDestinationId
                  }
              >
                {routeLoading ? 'Finding route...' : 'Find Route →'}
              </button>

              {route && (
                  <button className="map-clear-btn" onClick={clearRoute}>
                    Clear route
                  </button>
              )}
            </div>

            {(destinationsLoading || mapLoading) && (
                <p className="map-status">Loading map information...</p>
            )}

            {error && <p className="map-error">{error}</p>}

            {route && (
                <div className="map-route-info">
                  <strong>Route found</strong>
                  <p>
                    Main Entrance → {selectedDestination?.name}
                  </p>
                  {route.totalDistance != null && (
                      <p>Distance: {route.totalDistance}</p>
                  )}
                </div>
            )}
          </div>

          <div className="map-image-container">
            <img
                src="/Expo%20MockMap.jpg"
                alt="Expo Riyadh map"
            />

            {route && (
                <svg
                    className="map-route-svg"
                    viewBox="0 0 1000 1000"
                    preserveAspectRatio="none"
                    aria-label="Calculated route"
                >
                  <polyline
                      className="map-route-line"
                      points={route.points
                          .map(
                              (point) =>
                                  `${Number(point.x) * 1000},${Number(point.y) * 1000}`
                          )
                          .join(' ')}
                  />

                  {route.points.map((point, index) => (
                      <circle
                          key={`${point.nodeId ?? point.name}-${index}`}
                          cx={Number(point.x) * 1000}
                          cy={Number(point.y) * 1000}
                          r={index === 0 || index === route.points.length - 1 ? 12 : 7}
                          className={
                            index === 0
                                ? 'map-route-point map-route-start'
                                : index === route.points.length - 1
                                    ? 'map-route-point map-route-end'
                                    : 'map-route-point'
                          }
                      />
                  ))}
                </svg>
            )}
          </div>

          <div className="map-overlay">
            <span className="map-pin">⌖</span>
            <div>
              <strong>
                {route
                    ? `Route to ${selectedDestination?.name}`
                    : 'Explore Expo Riyadh'}
              </strong>
              <p>
                {route
                    ? 'Your route is displayed on the map.'
                    : 'Find pavilions, services, and destinations.'}
              </p>
            </div>
          </div>
        </div>
      </section>
  )

  const renderDashboard = () => (
      <>
        <section className="welcome-card">
          <div className="welcome-content">
            <span className="eyebrow">YOUR EXPO, ALL IN ONE</span>
            <h2>Your journey starts here.</h2>
            <p>
              Discover destinations, navigate Expo Riyadh, and access
              your visitor services in one convenient place.
            </p>
            <button
                className="primary-btn"
                onClick={() => navigateTo('map')}
            >
              Explore the map <span>→</span>
            </button>
          </div>

          <div className="welcome-art" aria-hidden="true">
            <Logo large />
            <span className="art-star star-one">✦</span>
            <span className="art-star star-two">✧</span>
          </div>
        </section>

        <section className="section">
          <div className="section-heading">
            <div>
              <h2>Explore our services</h2>
              <p>Everything you need for your Expo visit.</p>
            </div>
            <span className="section-count">04 SERVICES</span>
          </div>

          <div className="feature-grid">
            <button className="feature-card" onClick={() => navigateTo('identity')}>
              <span className="feature-icon purple">◎</span>
              <span className="feature-arrow">↗</span>
              <h3>Digital Identity</h3>
              <p>Your visitor identity in one convenient place.</p>
              <span className="feature-link">Explore identity <span>→</span></span>
            </button>

            <button className="feature-card" onClick={() => navigateTo('map')}>
              <span className="feature-icon blue">⌖</span>
              <span className="feature-arrow">↗</span>
              <h3>Smart Interactive Map</h3>
              <p>Discover destinations and find your route.</p>
              <span className="feature-link">Open map <span>→</span></span>
            </button>

            <button className="feature-card" onClick={() => navigateTo('passport')}>
              <span className="feature-icon orange">▤</span>
              <span className="feature-arrow">↗</span>
              <h3>Digital Passport</h3>
              <p>Your Expo visitor passport services.</p>
              <span className="feature-link">View passport <span>→</span></span>
            </button>

            <button className="feature-card" onClick={() => navigateTo('wallet')}>
              <span className="feature-icon green">◈</span>
              <span className="feature-arrow">↗</span>
              <h3>Digital Wallet</h3>
              <p>Manage your wallet and Expo payments.</p>
              <span className="feature-link">Open wallet <span>→</span></span>
            </button>
          </div>
        </section>

        <section className="section">
          <div className="section-heading">
            <div>
              <h2>Discover Expo</h2>
              <p>Start exploring the Expo grounds.</p>
            </div>
            <button className="text-action" onClick={() => navigateTo('map')}>
              View map →
            </button>
          </div>

          <button className="map-preview-card" onClick={() => navigateTo('map')}>
            <img src="/Expo%20MockMap.jpg" alt="Expo Riyadh map preview" />
            <div className="map-preview-caption">
              <span className="map-pin">⌖</span>
              <div>
                <strong>Expo Riyadh Interactive Map</strong>
                <p>Find pavilions, services, and routes.</p>
              </div>
              <span className="map-arrow">→</span>
            </div>
          </button>
        </section>
      </>
  )

  const renderPlaceholder = (title, description) => (
      <section className="section">
        <div className="section-heading">
          <div>
            <h2>{title}</h2>
            <p>{description}</p>
          </div>
        </div>
        <div className="service-placeholder">
          <Logo large />
          <h3>{title}</h3>
          <p>
            This section is awaiting backend integration.
            User-dependent features have not been connected yet.
          </p>
          <button className="map-action-btn" onClick={() => navigateTo('dashboard')}>
            Back to dashboard
          </button>
        </div>
      </section>
  )

  return (
      <div className="app-layout">
        <aside className="sidebar">
          <button className="brand brand-button" onClick={() => navigateTo('dashboard')}>
            <Logo />
            <span>
            <strong>محور <span className="brand-english">MIHWAR</span></strong>
            <small>YOUR EXPO, ALL IN ONE</small>
          </span>
          </button>

          <p className="menu-label">MAIN MENU</p>

          <nav className="navigation">
            {[
              ['dashboard', '⌂', 'Dashboard'],
              ['identity', '◎', 'Digital Identity'],
              ['map', '⌖', 'Interactive Map'],
              ['passport', '▤', 'Digital Passport'],
              ['wallet', '◈', 'Digital Wallet'],
            ].map(([id, icon, label]) => (
                <button
                    key={id}
                    className={`nav-link ${activeSection === id ? 'active' : ''}`}
                    onClick={() => navigateTo(id)}
                >
                  <span>{icon}</span>
                  {label}
                </button>
            ))}
          </nav>

          <div className="sidebar-bottom">
            <div className="help-card">
              <span>✦</span>
              <strong>Need some help?</strong>
              <p>Explore MIHWAR services to make your Expo visit easier.</p>
              <button onClick={() => navigateTo('map')}>Explore the map →</button>
            </div>
            <small className="copyright">© MIHWAR 2026</small>
          </div>
        </aside>

        <main className="main-content">
          <header className="topbar">
            <div>
            <span className="breadcrumb">
              MIHWAR / {pageInfo[activeSection][0]}
            </span>
              <h1>{pageInfo[activeSection][0]}</h1>
              <p className="subtitle">{pageInfo[activeSection][1]}</p>
            </div>

            <div className="profile">
              <span className="avatar">M</span>
              <span>
              <strong>Expo Visitor</strong>
              <small>Welcome to MIHWAR</small>
            </span>
            </div>
          </header>

          {activeSection === 'dashboard' && renderDashboard()}
          {activeSection === 'map' && renderMap()}
          {activeSection === 'identity' &&
              renderPlaceholder('Digital Identity', 'Your visitor identity and essential information.')}
          {activeSection === 'passport' &&
              renderPlaceholder('Digital Passport', 'Your Expo visitor passport services.')}
          {activeSection === 'wallet' &&
              renderPlaceholder('Digital Wallet', 'Your wallet and payment services.')}

          <footer className="page-footer">
            <span>© 2026 MIHWAR. All rights reserved.</span>
            <span>Your Expo, All in One.</span>
          </footer>
        </main>
      </div>
  )
}

export default App