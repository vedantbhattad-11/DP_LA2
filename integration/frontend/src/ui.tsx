import { useCallback, useEffect, useMemo, useState } from 'react';
import type { FormEvent } from 'react';
import { api } from './api';
import type { FailureType, Options, Transaction } from './api';

type Notice = { kind: 'success' | 'error'; text: string } | null;
const currency = (value: number) => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(value);
const time = (value: string) => new Date(value).toLocaleString('en-IN', { dateStyle: 'medium', timeStyle: 'short' });
const methodMark = (value: string) => ({ UPI: 'U', Card: '▰', 'Net Banking': '▤', Wallet: '◫' }[value] || '↗');
const errorText = (error: unknown) => error instanceof Error ? error.message : 'The request failed. Try again.';

function App() {
  const [options, setOptions] = useState<Options | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [selected, setSelected] = useState<Transaction | null>(null);
  const [activeTab, setActiveTab] = useState<'payment' | 'failure'>('payment');
  const [notice, setNotice] = useState<Notice>(null);
  const [loading, setLoading] = useState(false);
  const [refreshing, setRefreshing] = useState(true);
  const [pay, setPay] = useState({ amount: '', method: '', provider: '', userId: '' });
  const [failure, setFailure] = useState({ amount: '', method: '', provider: '', failureType: '', retrySucceeds: true });

  const refresh = useCallback(async () => {
    try { const result = await api.transactions(); setTransactions(result.slice().reverse()); }
    catch (error) { setNotice({ kind: 'error', text: errorText(error) }); }
    finally { setRefreshing(false); }
  }, []);

  useEffect(() => {
    let alive = true;
    api.options().then((result) => {
      if (!alive) return;
      setOptions(result);
      const method = result.methods[0] || ''; const provider = result.providers[0] || ''; const failureType = result.failureTypes[0]?.code || '';
      setPay(current => ({ ...current, method, provider }));
      setFailure(current => ({ ...current, method, provider, failureType }));
    }).catch((error: unknown) => { if (alive) setNotice({ kind: 'error', text: errorText(error) }); });
    void refresh();
    return () => { alive = false; };
  }, [refresh]);

  const totals = useMemo(() => ({
    count: transactions.length,
    succeeded: transactions.filter(item => item.status === 'SUCCESS').length,
    volume: transactions.filter(item => item.status === 'SUCCESS').reduce((sum, item) => sum + item.amount, 0),
    attention: transactions.filter(item => ['FAILED', 'ESCALATED'].includes(item.status)).length
  }), [transactions]);

  async function submitPayment(event: FormEvent) {
    event.preventDefault(); if (!options) return;
    setLoading(true); setNotice(null);
    try {
      const txn = await api.payment({ ...pay, amount: Number(pay.amount) });
      setNotice({ kind: 'success', text: `Payment completed. Transaction ${txn.transactionId}.` });
      setPay(current => ({ ...current, amount: '' })); await refresh();
    } catch (error) {
      const txn = (error as Error & { transaction?: Transaction }).transaction;
      setNotice({ kind: 'error', text: txn ? `Payment ${txn.status.toLowerCase()}: ${txn.failureReason || 'check payment details'}. Transaction ${txn.transactionId}.` : errorText(error) });
      await refresh();
    } finally { setLoading(false); }
  }

  async function submitFailure(event: FormEvent) {
    event.preventDefault(); if (!options) return;
    setLoading(true); setNotice(null);
    try {
      const txn = await api.failure({ ...failure, amount: Number(failure.amount) });
      setNotice({ kind: 'success', text: `Retry resolved the simulated failure. Transaction ${txn.transactionId}.` });
      setFailure(current => ({ ...current, amount: '' })); await refresh();
    } catch (error) {
      const txn = (error as Error & { transaction?: Transaction }).transaction;
      setNotice({ kind: 'error', text: txn ? `${txn.retryOutcome?.replace(/_/g, ' ') || (txn.status === 'ESCALATED' ? 'Escalated to support' : 'Failure remains unresolved')}: ${txn.failureType || txn.failureReason}. Transaction ${txn.transactionId}.` : errorText(error) });
      await refresh();
    } finally { setLoading(false); }
  }

  async function openDetails(id: string) {
    try { setSelected(await api.transaction(id)); }
    catch (error) { setNotice({ kind: 'error', text: errorText(error) }); }
  }

  return <div className="layout">
    <aside className="sidebar">
      <a className="brand" href="#top"><span className="brand-icon"><i/><i/><i/><i/></span><span>northstar<small>PAYMENTS LAB</small></span></a>
      <div className="workspace"><span className="workspace-mark">N</span><span><b>Northstar Studio</b><small>Demo workspace</small></span><span className="workspace-arrows">⌃<br/>⌄</span></div>
      <div className="nav-caption">WORKSPACE</div><button className="side-link current"><span>▦</span>Overview</button><button className="side-link" onClick={() => document.querySelector('#transactions')?.scrollIntoView({ behavior: 'smooth' })}><span>↕</span>Transactions</button>
      <div className="nav-caption tools-caption">PAYMENT TOOLS</div><button className={`side-link ${activeTab === 'payment' ? 'current' : ''}`} onClick={() => { setActiveTab('payment'); document.querySelector('#action-panel')?.scrollIntoView({ behavior: 'smooth' }); }}><span>＋</span>New payment</button><button className={`side-link ${activeTab === 'failure' ? 'current' : ''}`} onClick={() => { setActiveTab('failure'); document.querySelector('#action-panel')?.scrollIntoView({ behavior: 'smooth' }); }}><span>⌁</span>Failure simulator</button>
      <div className="side-note"><span className="note-orbit">◌</span><b>Demo environment</b><p>All gateways are simulated. This app never moves money or verifies identity.</p><span className="note-tag">LOCAL WORKFLOW</span></div>
      <div className="side-user"><span className="user-avatar">D</span><span><b>Demo operator</b><small>Local session</small></span><span className="user-dots">···</span></div>
    </aside>

    <main className="main" id="top">
      <header className="topbar"><div className="breadcrumbs"><span>Northstar Studio</span><i>/</i><b>Payment overview</b></div><div className="topbar-right"><span className="test-chip"><i/>TEST MODE</span><span className="top-divider"/><span className="user-avatar small-avatar">D</span></div></header>
      <div className="content">
        <div className="hero"><div><div className="date-kicker"><i/> PAYMENT OPERATIONS <span>·</span> LOCAL DEMO</div><h1>Payments, <em>in motion.</em></h1><p>Explore a simulated workflow powered by the patterns in this project.</p></div><div className="hero-mark" aria-hidden="true"><span>↗</span><i/><i/><i/></div></div>
        <div className="demo-banner"><span className="banner-symbol">ⓘ</span><span><b>Simulation only.</b> No real payment details are requested. Entered IDs are labels, not authentication.</span><span className="banner-right">IN-MEMORY <i/></span></div>
        <section className="stats">
          <article className="stat"><div className="stat-label">Processed volume <span className="stat-mark mark-green">↗</span></div><strong>{currency(totals.volume)}</strong><small>Successful transactions in this session</small><div className="stat-spark green-spark"><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/></div></article>
          <article className="stat"><div className="stat-label">Transactions <span className="stat-mark mark-blue">↕</span></div><strong>{totals.count}</strong><small>Recorded since the backend started</small><div className="stat-line"><i style={{ width: `${Math.min(100, totals.count * 12)}%` }}/></div><div className="stat-legend"><span>LIVE SESSION</span><span>{totals.count} total</span></div></article>
          <article className="stat"><div className="stat-label">Completed <span className="stat-mark mark-lilac">✓</span></div><strong>{totals.succeeded}</strong><small>Successful simulated payments</small><div className="stat-progress"><span style={{ width: `${totals.count ? Math.round(totals.succeeded / totals.count * 100) : 0}%` }}/></div><div className="stat-legend"><span>SUCCESS RATE</span><span>{totals.count ? Math.round(totals.succeeded / totals.count * 100) : 0}%</span></div></article>
          <article className="stat"><div className="stat-label">Needs attention <span className="stat-mark mark-amber">!</span></div><strong>{totals.attention}</strong><small>Failed or escalated transactions</small><div className="attention-state"><i/><span>{totals.attention ? 'Review recent activity' : 'All clear in this session'}</span></div></article>
        </section>

        <section className="columns">
          <section className="panel activity" id="transactions"><div className="panel-head"><div><div className="section-kicker">SESSION ACTIVITY</div><h2>Transactions</h2><p>Every request handled by this running backend.</p></div><button className="refresh" onClick={() => void refresh()} disabled={refreshing}>↻ <span>{refreshing ? 'Loading' : 'Refresh'}</span></button></div>
            {refreshing && !transactions.length ? <div className="empty-state"><span className="spinner"/>Loading transactions…</div> : transactions.length === 0 ? <div className="empty-state"><span className="empty-icon">↕</span><b>No transactions yet</b><span>Process a demo payment to see the workflow here.</span></div> : <div className="table-scroll"><table><thead><tr><th>TRANSACTION</th><th>METHOD / GATEWAY</th><th>AMOUNT</th><th>STATUS</th><th/></tr></thead><tbody>{transactions.map(txn => <tr key={txn.transactionId}><td><button className="txn-button" onClick={() => void openDetails(txn.transactionId)}><span>{txn.transactionId}</span><small>{time(txn.timestamp)}</small></button></td><td><div className="method-cell"><span className="method-badge">{methodMark(txn.method)}</span><span>{txn.method}<small>{txn.provider}</small></span></div></td><td className="amount-cell">{currency(txn.amount)}</td><td><span className={`status status-${txn.status.toLowerCase()}`}><i/>{txn.status}</span></td><td><button className="detail-arrow" aria-label={`Details for ${txn.transactionId}`} onClick={() => void openDetails(txn.transactionId)}>↗</button></td></tr>)}</tbody></table></div>}
            <div className="panel-foot"><span>{transactions.length} {transactions.length === 1 ? 'record' : 'records'} <i>·</i> resets when backend stops</span><span className="foot-live"><i/> CONNECTED</span></div>
          </section>

          <section className="panel action-panel" id="action-panel"><div className="panel-head action-head"><div><div className="section-kicker">WORKFLOW PLAYGROUND</div><h2>Run a scenario</h2><p>Invokes the original Java payment components.</p></div></div><div className="tabs"><button className={activeTab === 'payment' ? 'selected-tab' : ''} onClick={() => { setActiveTab('payment'); setNotice(null); }}>New payment</button><button className={activeTab === 'failure' ? 'selected-tab' : ''} onClick={() => { setActiveTab('failure'); setNotice(null); }}>Failure test</button></div>
            {!options ? <div className="backend-state"><span className="warning-icon">!</span><b>Waiting for the backend</b><p>Start Spring Boot on port 8080. The payment form uses the backend to load its available methods and gateways.</p></div> : activeTab === 'payment' ? <form className="form" onSubmit={submitPayment}>
              <label>AMOUNT <span>IN INR</span></label><div className="money-input"><span>₹</span><input required type="number" min="0.01" max="1000000" step="0.01" placeholder="0.00" value={pay.amount} onChange={event => setPay({ ...pay, amount: event.target.value })}/></div>
              <div className="form-row"><div><label htmlFor="method">PAYMENT METHOD</label><select id="method" value={pay.method} onChange={event => setPay({ ...pay, method: event.target.value })}>{options.methods.map(method => <option key={method}>{method}</option>)}</select></div><div><label htmlFor="provider">GATEWAY</label><select id="provider" value={pay.provider} onChange={event => setPay({ ...pay, provider: event.target.value })}>{options.providers.map(provider => <option key={provider}>{provider}</option>)}</select></div></div>
              <label htmlFor="userId">ENTERED USER ID <span>NOT VERIFIED</span></label><input id="userId" required maxLength={120} placeholder="Any non-empty label" value={pay.userId} onChange={event => setPay({ ...pay, userId: event.target.value })}/>
              <div className="method-pills">{options.methods.map(method => <button type="button" className={pay.method === method ? 'pill-active' : ''} onClick={() => setPay({ ...pay, method })} key={method}><i>{methodMark(method)}</i>{method}</button>)}</div>
              <button className="submit" type="submit" disabled={loading || !options}>{loading ? <><span className="spinner light"/>Processing with Java workflow…</> : <>Process demo payment <span>→</span></>}</button>
            </form> : <form className="form" onSubmit={submitFailure}>
              <div className="scenario-intro"><span className="scenario-icon">⌁</span><span><b>Exercise the failure chain</b><small>Validation → Gateway → Retry → Support</small></span></div>
              <label>AMOUNT <span>IN INR</span></label><div className="money-input"><span>₹</span><input required type="number" min="0.01" max="1000000" step="0.01" placeholder="0.00" value={failure.amount} onChange={event => setFailure({ ...failure, amount: event.target.value })}/></div>
              <div className="form-row"><div><label>PAYMENT METHOD</label><select value={failure.method} onChange={event => setFailure({ ...failure, method: event.target.value })}>{options.methods.map(method => <option key={method}>{method}</option>)}</select></div><div><label>GATEWAY</label><select value={failure.provider} onChange={event => setFailure({ ...failure, provider: event.target.value })}>{options.providers.map(provider => <option key={provider}>{provider}</option>)}</select></div></div>
              <label>FAILURE TYPE</label><select value={failure.failureType} onChange={event => setFailure({ ...failure, failureType: event.target.value })}>{options.failureTypes.map((item: FailureType) => <option value={item.code} key={item.code}>{item.code.replace(/_/g, ' ')}</option>)}</select>
              <label className="retry-toggle"><span><b>Retry succeeds</b><small>Turn off to exercise support escalation.</small></span><input type="checkbox" checked={failure.retrySucceeds} onChange={event => setFailure({ ...failure, retrySucceeds: event.target.checked })}/><i/></label>
              <button className="submit" type="submit" disabled={loading || !options}>{loading ? <><span className="spinner light"/>Running failure chain…</> : <>Simulate failure <span>→</span></>}</button>
            </form>}
            {notice && <div role="status" className={`notice notice-${notice.kind}`}><span>{notice.kind === 'success' ? '✓' : '!'}</span>{notice.text}<button aria-label="Dismiss" onClick={() => setNotice(null)}>×</button></div>}
            <div className="action-foot"><span>▤</span> Powered by the project’s existing Java patterns.</div>
          </section>
        </section>
        <footer><span>Northstar Payments Lab <i>·</i> Local integration demo</span><span>Java workflow <i>·</i> Spring Boot API <i>·</i> In-memory only</span></footer>
      </div>
    </main>
    {selected && <div className="overlay" role="presentation" onMouseDown={event => { if (event.target === event.currentTarget) setSelected(null); }}><section className="detail-modal" role="dialog" aria-modal="true" aria-labelledby="detail-title"><div className="detail-top"><span className="section-kicker">TRANSACTION RECORD</span><button onClick={() => setSelected(null)} aria-label="Close">×</button></div><div className="detail-id">{selected.transactionId}</div><span className={`status status-${selected.status.toLowerCase()}`}><i/>{selected.status}</span><div className="detail-amount">{currency(selected.amount)}</div><dl><div><dt>Payment method</dt><dd>{selected.method}</dd></div><div><dt>Gateway</dt><dd>{selected.provider}</dd></div><div><dt>Recorded</dt><dd>{time(selected.timestamp)}</dd></div>{selected.failureReason && <div><dt>Failure / outcome</dt><dd>{selected.failureReason}</dd></div>}</dl><p>This demo record contains no credential or identity data.</p></section></div>}
  </div>;
}
export default App;
